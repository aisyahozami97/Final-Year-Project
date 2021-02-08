package com.example.preloveditemandevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class adminHomePage extends AppCompatActivity {

    Button sales, purchaseOrder, event, listUser, btnSignout;
    FirebaseUser user;
    FirebaseAuth auth;

    BottomNavigationView bottomNavigationView;
    Button btnSignOut, btnNews, btnItem, btnListUser, btnListVolunteer, btnPurchaseOrder, btnProfile;
    FirebaseAuth mAuth;

    ProgressDialog PD;



    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_adminhomepage);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //CheckBooking

        btnNews = (Button) findViewById(R.id.btn_news);
        btnItem = (Button) findViewById(R.id.btn_item);
        btnListUser = (Button) findViewById(R.id.btn_listAccount);
        btnListVolunteer = (Button) findViewById(R.id.btn_listVolunteer);
        btnPurchaseOrder = (Button) findViewById(R.id.btn_purchasedOrder);
        btnProfile = (Button) findViewById(R.id.btn_profileAdmin);

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(adminHomePage.this, MainNews.class));
               // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });
        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(adminHomePage.this, MainItem.class));
                // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });

        btnListUser.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(adminHomePage.this, ListUserActivity.class));
                // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });
        btnListVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(adminHomePage.this, ListVolunteerActivity.class));
                // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });

        btnPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(adminHomePage.this, ListPurchaseOrder.class));
                // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(adminHomePage.this, adminProfile.class));
                // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });


        btnSignOut = (Button) findViewById(R.id.btn_signOut);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(adminHomePage.this, Login_Form.class));
                finish();
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            startActivity(new Intent(adminHomePage.this, Login_Form.class));
                            finish();
                        }
                    }
                };
            }
        });
        btnListUser.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(adminHomePage.this, ListUserActivity.class));
                // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });



        //bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
       // bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           // @Override
            //public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //switch (menuItem.getItemId()) {
                    //case R.id.action_home:
                        //Toast.makeText(adminHomePage.this, "Home Button Clicked", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), adminHomePage.class));
                        //break;
                    //case R.id.action_Profile:
                       // Toast.makeText(adminHomePage.this, "Profile Button Clicked", Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(getApplicationContext(), adminProfile.class));
                        //break;
                   // case R.id.action_statistic:
                   //     Toast.makeText(adminHomePage.this, "Statistic Button Clicked", Toast.LENGTH_SHORT).show();
                    //    startActivity(new Intent(getApplicationContext(), MainNews.class));
                      //  break;
                //}return true; }});




    }




}