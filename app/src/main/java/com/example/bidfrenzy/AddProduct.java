package com.example.bidfrenzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddProduct extends AppCompatActivity {

    private EditText productNameEdt,productCategoryEdt,productPriceEdt,productImageEdt,productDescriptionEdt;
    private Button addProductBtn;
    private ProgressBar loadingPB;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduct);
        productNameEdt = findViewById(R.id.ProductName);
        productCategoryEdt = findViewById(R.id.ProductCategory);
        productPriceEdt = findViewById(R.id.ProductPrice);
        productImageEdt = findViewById(R.id.ProductImageLink);
        productDescriptionEdt = findViewById(R.id.ProductDescription);
        addProductBtn = findViewById(R.id.BtnAddProduct);
        loadingPB = findViewById(R.id.PBloading);
        db = FirebaseFirestore.getInstance();


        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String productName = productNameEdt.getText().toString();
                String productCategory = productCategoryEdt.getText().toString();
                String productPrice = productPriceEdt.getText().toString();
                String productImageLink = productImageEdt.getText().toString();
                String productDescription = productDescriptionEdt.getText().toString();
                String productID = productName;


                saveToFireStore(productID,productName, productCategory,productPrice,productImageLink,productDescription);
            }
        });
    }

    private void saveToFireStore(String productID,String productName,String productCategory,String productPrice,String productImageLink,String productDescription){
        if (!productName.isEmpty() && !productCategory.isEmpty() && !productPrice.isEmpty() && !productImageLink.isEmpty() && !productDescription.isEmpty()){
            HashMap<String,Object> map = new HashMap<>();
            map.put("productName", productName);
            map.put("productPrice", productPrice);
            map.put("productCategory", productCategory);
            map.put("productImage", productImageLink);
            map.put("productDescription", productDescription);
            map.put("productID", productID);

            db.collection("Products").document(productID).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(AddProduct.this, "Product Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddProduct.this, AdminBrowseProducts.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
                }
            });
        }else
            Toast.makeText(this, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show();

    }
}