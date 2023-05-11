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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText mEmail, mpassword;
    Button mLoginBtn;
    ProgressBar progressBar;
    TextView createText;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    DocumentReference reference;
    String userType="Customer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mEmail = findViewById(R.id.Email);
        mpassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar2);
        createText = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mpassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    mEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    mpassword.setError("Password is Required");
                    mpassword.requestFocus();
                } else if (password.length() < 6) {
                    mpassword.setError("Password should be of >=6 characters");
                    mpassword.requestFocus();
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = fAuth.getInstance().getCurrentUser();

                                String UserID = user.getUid();
                                db= FirebaseFirestore.getInstance();
                                reference=db.collection("Users").document(UserID);
                                reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if(task.getResult().exists()){

                                                    String TypeResult = task.getResult().getString("UserType").trim();

                                                    if(TypeResult.equalsIgnoreCase(userType)){
                                                        startActivity(new Intent(Login.this, AdminDashboard.class));
                                                    }else{
                                                        startActivity(new Intent(Login.this, AdminDashboard.class));
                                                    }
                                                    finish();
                                                }
                                            }
                                        });

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                    //login

                }
            }
        });
    }

    public void register(){
        startActivity(new Intent(this, Register.class));
    }
}






