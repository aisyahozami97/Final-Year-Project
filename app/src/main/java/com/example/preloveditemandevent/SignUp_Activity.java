package com.example.preloveditemandevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp_Activity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;
    private Button btn_signUp, btn_Login;
    private ProgressDialog PD;

    private String authData;
    DatabaseReference databaseCust;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_signup_form);
        getSupportActionBar().setTitle("Signup Form");

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //progressBar.setVisibility(View.GONE);
        //progressBar = findViewById(R.id.progressBar);

        //object initialize
        mAuth = FirebaseAuth.getInstance();

        databaseCust = FirebaseDatabase.getInstance().getReference("User");

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUp_Activity.this, MainActivity.class));
            finish();
        }

        inputEmail = (EditText) findViewById(R.id.txt_email);
        inputPassword = (EditText) findViewById(R.id.txt_password);
        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_Login = (Button) findViewById(R.id.btn_login);
        //findViewById(R.id.btn_signUp).setOnClickListener(this);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                //String confirmPassword = txt_confirmPassword.getText().toString().trim();

                try {

                    if (password.length() > 0 && email.length() > 0) {
                        PD.show();
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUp_Activity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(
                                                    SignUp_Activity.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                            Log.v("error", task.getResult().toString());
                                        } else {

                                            authData = mAuth.getUid();
                                            //String id = databaseCust.push().getKey();

                                            String userid = authData;
                                            String firstname = "null";
                                            String lastname = "null";
                                            String userName = "null";
                                            String phone = "null";
                                            String email1 = email;
                                            String address = "null";


                                            Log.i("Go Through Here", userid);
                                            User user = new User( userid, firstname, lastname, userName, phone, email, address);

                                            databaseCust.child(authData).setValue(user);

                                            Intent intent = new Intent(SignUp_Activity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                SignUp_Activity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}

