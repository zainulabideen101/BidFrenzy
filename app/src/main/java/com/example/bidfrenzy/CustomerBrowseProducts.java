package com.example.bidfrenzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerBrowseProducts extends AppCompatActivity implements ProductRVAdapter.ProductClickInterface{

    private RecyclerView productRV;
    private FloatingActionButton addFAB;
    private FirebaseFirestore db;
    public static ArrayList<ProductRVModal> productRVModalArrayList;
    private RelativeLayout bottomSheetRL;
    private ProductRVAdapter productRVAdapter;
    private FirebaseAuth fAuth;
    private String Category="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browseproducts);
        productRV = findViewById(R.id.idRVProducts);
        productRV.setHasFixedSize(true);
        productRV.setLayoutManager(new LinearLayoutManager(this));

        Category = "";
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            Category=bundle.getString("Category");
        }
        db = FirebaseFirestore.getInstance();
        productRVModalArrayList = new ArrayList<>();
        //bottomSheetRL = findViewById(R.id.idRLBSheet);
        fAuth = FirebaseAuth.getInstance();
        productRVAdapter = new ProductRVAdapter(productRVModalArrayList,this,this);
        productRV.setAdapter(productRVAdapter);



        if (Category == ""){
            getAllProducts1();
        }
        getAllProducts(Category);
    }

    public void getAllProducts(String C){
        C = Category;
        db.collection("Products").whereEqualTo("productCategory",Category).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        productRVModalArrayList.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            ProductRVModal productRVModal = new ProductRVModal(snapshot.getString("productName"),snapshot.getString("productPrice"),snapshot.getString("productCategory"),snapshot.getString("productImage"),snapshot.getString("productDescription"),snapshot.getString("productID"));
                            productRVModalArrayList.add(productRVModal);
                        }
                        productRVAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerBrowseProducts.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAllProducts1(){
        db.collection("Products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        productRVModalArrayList.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            ProductRVModal productRVModal = new ProductRVModal(snapshot.getString("productName"),snapshot.getString("productPrice"),snapshot.getString("productCategory"),snapshot.getString("productImage"),snapshot.getString("productDescription"),snapshot.getString("productID"));
                            productRVModalArrayList.add(productRVModal);
                        }
                        productRVAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerBrowseProducts.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onProductClick(int position) {
        displayBottomSheet(productRVModalArrayList.get(position),position);
    }

    private void displayBottomSheet(ProductRVModal productRVModal,int position){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView productNameTV = layout.findViewById(R.id.idTVProductName);
        TextView productDescTV = layout.findViewById(R.id.idTVDescription);
        TextView productCategoryTV = layout.findViewById(R.id.idTVCategory);
        TextView productPriceTV = layout.findViewById(R.id.idTVProductPrice);
        ImageView productIV = layout.findViewById(R.id.idIVProduct);
        Button addaToCartBtn = layout.findViewById(R.id.idBtnAddtoCart);

        productNameTV.setText(productRVModal.getProductName());
        productDescTV.setText(productRVModal.getProductDescription());
        productCategoryTV.setText(productRVModal.getProductCategory());
        productPriceTV.setText("Rs. "+ productRVModal.getProductPrice());
        Picasso.get().load(productRVModal.getProductImage()).into(productIV);

        addaToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference CartList = db.collection("Users").document(fAuth.getUid()).collection("CartList").document();
                ProductRVModal item = productRVModalArrayList.get(position);
                HashMap<String,Object> map = new HashMap<>();
                map.put("productName", item.getProductName());
                map.put("productPrice", item.getProductPrice());
                map.put("productCategory", item.getProductCategory());
                map.put("productImage", item.getProductImage());
                map.put("productDescription", item.getProductDescription());
                map.put("productID", item.getProductID());
                CartList.set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String id= CartList.getId();
                                String price= item.getProductPrice();
                                Toast.makeText(CustomerBrowseProducts.this, "Add To Cart Successful", Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                Intent i = new Intent(CustomerBrowseProducts.this,AddToCart.class);
                                bundle.putString("OrderID",id);
                                bundle.putString("productPrice",price);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerBrowseProducts.this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    public void BackArrowC(View view) {
        startActivity(new Intent(getApplicationContext(), CustomerCategories.class));
    }


}