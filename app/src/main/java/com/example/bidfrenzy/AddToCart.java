package com.example.bidfrenzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Stack;

public class AddToCart extends AppCompatActivity implements AddToCartRVAdapter.ProductClickInterface{

    private RecyclerView addToCartRV;
    private ProgressBar loadingPB;
    private FirebaseFirestore db;
    public static ArrayList<ProductRVModal> addToCartArrayList;
    private AddToCartRVAdapter addToCartRVAdapter;
    private FirebaseAuth fAuth;
    private TextView productName,productCategory,productPrice,TotalAmount,Amount;
    private Button checkout,clearcart;
    private ImageView productIV;
    private int position;
    private String idnum;
    private String total;
    Stack<String> cartitems = new Stack<String>();
    Stack<String> carttotal = new Stack<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtocart);
        addToCartRV = findViewById(R.id.idRVAddToCart);
        addToCartRV.setHasFixedSize(true);
        addToCartRV.setLayoutManager(new LinearLayoutManager(this));
        checkout = findViewById(R.id.idBtnCheckout);
        clearcart = findViewById(R.id.idBtnClearCart);
        Amount = findViewById(R.id.AmounttotalTV);
        db = FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            idnum=bundle.getString("OrderID");
            //total=bundle.getString("productPrice");
        }
        addToCartArrayList = new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        addToCartRVAdapter = new AddToCartRVAdapter(addToCartArrayList,this,this);
        addToCartRV.setAdapter(addToCartRVAdapter);
        //UpdateTotal(total);
        //Amount.setText(total);
        getCartProducts1(idnum);

        clearcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartitems.empty()){
                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                    startActivity(i);
                }
                clearcart();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Payment.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onProductClick(int position) {
    }

    public void getCartProducts1(String idnum) {
        db.collection("Users").document(fAuth.getUid()).collection("CartList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        addToCartArrayList.clear();
                        int totalsum =0;
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            ProductRVModal productRVModal = new ProductRVModal(snapshot.getString("productName"), snapshot.getString("productPrice"), snapshot.getString("productCategory"), snapshot.getString("productImage"), snapshot.getString("productDescription"), snapshot.getString("productID"));
                            addToCartArrayList.add(productRVModal);
                            cartitems.push(snapshot.getId());
                            String a = productRVModal.getProductPrice();
                            int ab = Integer.parseInt(a);
                            totalsum += ab;
                            String sumfinal = String.valueOf(totalsum);
                            Amount.setText("Rs "+sumfinal);
                        }
                        addToCartRVAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddToCart.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void BackArrow(View view){
        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
    }

    public void clearcart(){
        db.collection("Users").document(fAuth.getUid()).collection("CartList").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        cartitems.empty();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            String a = snapshot.getId();
                            db.collection("Users").document(fAuth.getUid()).collection("CartList").document(a).delete();
                        }
                        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                        Toast.makeText(AddToCart.this,"Cart Cleared",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddToCart.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}