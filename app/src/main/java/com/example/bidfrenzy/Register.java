package com.example.bidfrenzy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Register extends AppCompatActivity {

        private EditText fNameEdt, emailEdt,passwordEdt,phoneEdt;
        private Button mRegisterBtn;
        private TextView mLoginBtn;
        private FirebaseAuth fAuth;
        private FirebaseFirestore db1;
        private ProgressBar progressBar;
        private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


            fNameEdt= findViewById(R.id.fullName);
            emailEdt=findViewById(R.id.Email);
            passwordEdt=findViewById(R.id.password);
            phoneEdt=findViewById(R.id.phone);
            mRegisterBtn=findViewById(R.id.registerBtn);
            mLoginBtn=findViewById(R.id.createText1);

            fAuth= FirebaseAuth.getInstance();
            db1=FirebaseFirestore.getInstance();

            progressBar=findViewById(R.id.progressBar);

            mLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Register.this, Login.class));
                }

            });
            //VALIDATION of data entered

            mRegisterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String fName = fNameEdt.getText().toString();
                    String email = emailEdt.getText().toString();
                    String password = passwordEdt.getText().toString();
                    String phone = phoneEdt.getText().toString();
                    if(TextUtils.isEmpty(fName)){
                        fNameEdt.setError("Full Name is Required");
                        return;
                    }
                    if(TextUtils.isEmpty(email)){
                        emailEdt.setError("Email is required");
                        return;
                    }
                    if(TextUtils.isEmpty(password)){
                        passwordEdt.setError("Password is Required");
                        return;
                    }
                    if(TextUtils.isEmpty(phone)){
                        phoneEdt.setError("Phone number is Required");
                        return;
                    }
                    if(password.length()<6){
                        passwordEdt.setError("Password should be of >=6 characters");
                        return;
                    }
                    if(phone.length()!=11){
                        phoneEdt.setError("Should have 11 digits");
                        return;
                    }else{
                        //progressBar.setVisibility(View.VISIBLE);

                        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                                    UserID=fAuth.getCurrentUser().getUid();
                                    saveToFireStore(UserID,fName,email,password,phone,"Customer");
                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                }else {
                                    Toast.makeText(Register.this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }

                        });
                    }

                }
            });


    }
    private void saveToFireStore(String UserID,String fName,String email,String password,String phone, String type){
        type="Customer";
        int rating=0;
        HashMap<String,Object> map1 = new HashMap<>();
            map1.put("fName", fName);
            map1.put("email", email);
            map1.put("password", password);
            map1.put("phone", phone);
            map1.put("UserType", type);
            map1.put("Rating",rating);

            db1.collection("Users").document(UserID).set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //loadingPB.setVisibility(View.GONE);
                            Toast.makeText(Register.this, "User Data Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

}