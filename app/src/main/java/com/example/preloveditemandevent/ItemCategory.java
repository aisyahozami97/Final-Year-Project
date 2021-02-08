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

public class ItemCategory extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    Button btnSignOut;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog PD;

    private Toolbar mToolbar;
    private ImageView mLogout,mForgotPassword;

    private CardView cardAllItem;
    private CardView cardShoppingCart;
    private CardView cardClothes;
    private CardView cardTrousers;
    private CardView cardShoes;
    private CardView cardBags;


    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_item_category);

        //Cardview
        cardAllItem = (CardView)findViewById(R.id.card_allItem);
        cardShoppingCart = (CardView)findViewById(R.id.card_shoppingCart);
        cardClothes = (CardView)findViewById(R.id.card_clothes);
        cardTrousers = (CardView)findViewById(R.id.card_trousers);
        cardShoes = (CardView)findViewById(R.id.card_shoes);
        cardBags = (CardView)findViewById(R.id.card_bags);

        cardAllItem.setOnClickListener(this);
        cardShoppingCart.setOnClickListener(this);
        cardClothes.setOnClickListener(this);
        cardTrousers.setOnClickListener(this);
        cardShoes.setOnClickListener(this);
        cardBags.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.item_category_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("ITEM CATEGORY");



        //Logout
        mLogout = (ImageView) findViewById(R.id.btn_signOut);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ItemCategory.this,"Successfully Logout",Toast.LENGTH_SHORT).show();
                logout();
            }
        });

        //forgotpassword
        mForgotPassword = (ImageView) findViewById(R.id.btn_forgetPassword);
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ItemCategory.this,"Directly to Reset Password",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), forgetPasswordActivity.class).putExtra("Mode", 1));
            }
        });
        //if tk igt code forget password ni boleh rujuk w10 reference





    }

    @Override    protected void onResume() {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(ItemCategory.this, Login_Form.class));
            finish();
        }
        super.onResume();
    }

    //Logout Method
    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(ItemCategory.this, Login_Form.class));
        finish();
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(ItemCategory.this, Login_Form.class));
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.card_allItem : i = new Intent(this,MainItemUser.class);
                startActivity(i);
                break;

            case R.id.card_shoppingCart : i = new Intent(this,ShoppingCart.class);
                startActivity(i);
                break;

            case R.id.card_clothes: i = new Intent(this,MainNewsUser.class);
                startActivity(i);
                break;

            case R.id.card_trousers: i = new Intent(this,CustProfile.class);
                startActivity(i);
                break;

            case R.id.card_shoes: i = new Intent(this,CustProfile.class);
                startActivity(i);
                break;

            case R.id.card_bags: i = new Intent(this,CustProfile.class);
                startActivity(i);
                break;

            default:break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(ItemCategory.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}