package com.example.bidfrenzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReviewSeller extends AppCompatActivity {

    private ArrayList<User> userArrayList;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private DocumentReference reference;
    private TextView Name,Title,Email,Phone,reviews,rating;
    private TextView EditProf,ChangePW;
    String userType="Customer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewseller);

        Name= findViewById(R.id.UserFnameTV);
        Title=findViewById(R.id.UserProfTV);
        Email=findViewById(R.id.UserEmailTV);
        Phone=findViewById(R.id.UserPhoneTV);
        //reviews=findViewById(R.id.reviews);
        rating=findViewById(R.id.rating);

    }
    public void BackArrow(View view) {
        FirebaseUser user = fAuth.getInstance().getCurrentUser();
        String UserID = user.getUid();
        db= FirebaseFirestore.getInstance();
        reference=db.collection("Users").document(UserID);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.getResult().exists()){
                    String TypeResult = task.getResult().getString("UserType").trim();
                        startActivity(new Intent(ReviewSeller.this, AdminBrowseProducts.class));
                    finish();
                }
            }
        });
    }

    public void editprofile(View view){
        startActivity(new Intent(getApplicationContext(),EditUserProfile.class));
    }

    public void SignOut(View view) {
        startActivity(new Intent(getApplicationContext(),Welcome.class));
    }

    public void updateUserData(int position){
        User item = userArrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("fName", item.getUserName());
        bundle.putString("email", item.getUserEmail());
        bundle.putString("fName", item.getUserName());
        bundle.putString("phone", item.getPhoneNumber());
        bundle.putInt("position",position);
        Intent i = new Intent(getApplicationContext(), EditUserProfile.class);
        i.putExtras(bundle);
        startActivity(i);
    }



    public void onStart(){
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UserID = user.getUid();

        db=FirebaseFirestore.getInstance();
        reference=db.collection("Users").document(UserID);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.getResult().exists()){

                    String nameResult = task.getResult().getString("fName").trim();
                    String emailResult = task.getResult().getString("email");
                    String titleResult = task.getResult().getString("fName");
                    String phoneResult = task.getResult().getString("phone");

                    Name.setText(nameResult);
                    Title.setText(titleResult);
                    Email.setText(emailResult);
                    Phone.setText(phoneResult);


                }else{
                    Intent i = new Intent(getApplicationContext(), CustomerDashboard.class);
                    startActivity(i);
                }
            }
        });

    }
}