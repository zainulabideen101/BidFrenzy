package com.example.bidfrenzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Payment extends AppCompatActivity {

    int check=0;
    public Button Btn;
    public EditText trx;
    private FirebaseFirestore db;
    public static ArrayList<ProductRVModal> productRVModalArrayList;
    private FirebaseAuth fAuth;
    private EditText TransactionId,Address;
    private Stack<String> cartitems = new Stack<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        fAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        Btn = findViewById(R.id.BtnPlaceOrder);
        TransactionId = findViewById(R.id.TRXID);
        Address=findViewById(R.id.AddressDescription);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TransactionId.getText().toString().isEmpty()){
                    check++;
                    TransactionId.setError("Field cannot be empty");
                }
                else{
                    check=0;
                }
                if(Address.getText().toString().isEmpty()){
                    check++;
                    Address.setError("Field cannot be empty");
                }
                else{
                    check=0;
                }
                if(check == 0) {
                    GetTotal();
                }
            }
        });
    }

    public void BackArrow(View view) {
        startActivity(new Intent(getApplicationContext(),AddToCart.class));
    }

    public void GetTotal(){

        DocumentReference CartList = db.collection("Users").document(fAuth.getUid());
        CartList.collection("CartList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String a = null;
                        String b = null;
                        String c = null;
                        int total = 0;
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            ProductRVModal productRVModal = new ProductRVModal(snapshot.getString("productName"), snapshot.getString("productPrice"), snapshot.getString("productCategory"), snapshot.getString("productImage"), snapshot.getString("productDescription"), snapshot.getString("productID"));

                            if(a==null) {
                                a = productRVModal.getProductName();
                                c = productRVModal.getProductCategory();
                            }
                            else{
                                a = a + " , " + productRVModal.getProductName();
                                b = productRVModal.getProductPrice();
                                c = c + " , " + productRVModal.getProductCategory();
                                total = total + Integer.parseInt(b);

                            }

                        }
                        b = String.valueOf(total);
                        LoadBal(a,b,c);
                        String i = TransactionId.getText().toString();
                        String j = Address.getText().toString();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("Transaction ID", i);
                        map.put("Address", j);
                        db.collection("Orders").document(fAuth.getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Intent i = new Intent(Payment.this, AdminDashboard.class);
                                        startActivity(i);
                                        clearcart();
                                        Toast.makeText(Payment.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Payment.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Payment.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoadBal(String a,String b,String c){

        HashMap<String, Object> map = new HashMap<>();
        map.put("productName",a);
        map.put("productPrice",b);
        map.put("productCategory",c);
        map.put("orderStatus", "Pending");
        map.put("orderOf", fAuth.getCurrentUser().getEmail());
        ////map.put("customerName", fAuth.getCurrentUser().getDisplayName());
        db.collection("Orders").document().set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
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
                        //Toast.makeText(Payment.this,"Cart Cleared",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Payment.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}