package com.example.preloveditemandevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    Button btnSignOut;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog PD;

    private Toolbar mToolbar;
    private ImageView mLogout;

    private CardView cardPrelovedItem;
    private CardView cardNews;
    private CardView cardPurchaseOrder;
    private CardView cardProfile;


    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        //Cardview
        cardPrelovedItem = (CardView)findViewById(R.id.card_prelovedItem);
        cardNews = (CardView)findViewById(R.id.card_news);
        cardPurchaseOrder = (CardView)findViewById(R.id.card_purchaseOrder);
        cardProfile = (CardView)findViewById(R.id.card_profile);

        cardPrelovedItem.setOnClickListener(this);
        cardNews.setOnClickListener(this);
        cardPurchaseOrder.setOnClickListener(this);
        cardProfile.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_admin_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("USER HOMEPAGE");

        //Logout
        mLogout = (ImageView) findViewById(R.id.btn_signOut);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Successfully Logout",Toast.LENGTH_SHORT).show();
                logout();
            }
        });

        //forgotpassword
        //mShoppingCart = (ImageView) findViewById(R.id.btn_shoppingCart);
       // mShoppingCart.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"Directly to Shopping Cart",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), ShoppingCart.class).putExtra("Mode", 1));
            //}});
        //if tk igt code forget password ni boleh rujuk w10 reference





    }

    @Override    protected void onResume() {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, Login_Form.class));
            finish();
        }
        super.onResume();
    }

    //Logout Method
    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, Login_Form.class));
        finish();
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, Login_Form.class));
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.card_prelovedItem : i = new Intent(this,MainItemUser.class);
                startActivity(i);
                break;

            case R.id.card_news: i = new Intent(this,MainNewsUser.class);
                startActivity(i);
                break;

            case R.id.card_purchaseOrder: i = new Intent(this,PurchaseOrderUser.class);
                startActivity(i);
                break;

            case R.id.card_profile: i = new Intent(this,CustProfile.class);
                startActivity(i);
                break;

            default:break;
        }
    }
}