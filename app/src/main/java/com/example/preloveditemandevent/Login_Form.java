package com.example.preloveditemandevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login_Form extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;
    Button btn_signUp, btn_login;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_login_form);
        getSupportActionBar().setTitle("Login Form");

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        mAuth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.txt_email);
        inputPassword = (EditText) findViewById(R.id.txt_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signUp = (Button)findViewById(R.id.btn_signUp);

        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                try{
                    if (password.length() > 0 && email.length() > 0) {
                        PD.show();
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login_Form.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(
                                                    Login_Form.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                            Log.v("error", task.getResult().toString());
                                        } else {
                                            Log.i("login email",email);
                                            if(email.equals("adminkjum2@gmail.com")){

                                                Log.i("login email masuk",email);
                                                Intent intent = new Intent(Login_Form.this, adminHomePage.class);
                                                startActivity(intent);
                                                finish();

                                            }else{
                                                Intent intent = new Intent(Login_Form.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                Login_Form.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                Intent intent = new Intent(Login_Form.this, SignUp_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_forgetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), forgetPasswordActivity.class).putExtra("Mode", 0));
            }
        });

    }

    @Override    protected void onResume() {
        final String email = inputEmail.getText().toString();
        if (mAuth.getCurrentUser() != null) {

            if(mAuth.getCurrentUser().getEmail().equals("admin123@gmail.com") ){

                Intent intent = new Intent(Login_Form.this, adminHomePage.class);
                startActivity(intent);
                finish();

            }else{


                Intent intent = new Intent(Login_Form.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

        }
        super.onResume();
    }
}