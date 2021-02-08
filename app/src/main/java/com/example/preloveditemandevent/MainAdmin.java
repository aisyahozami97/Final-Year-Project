package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdmin extends AppCompatActivity implements View.OnClickListener  {

    private static final String TAG = "Profile";
    private Toolbar mToolbar;
    private ImageView mLogout;
    private TextView mViewName;
    private TextView mViewIc;
    private TextView mViewPhone;
    private TextView mViewEmail;
    private Button button;
    private CircleImageView profileimage;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String userID;

    private static String imgurl;

    private CardView cardPlace;
    private CardView cardNews;
    private CardView cardFacility;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        //Cardview
        cardPlace = (CardView)findViewById(R.id.card_place);
        cardNews = (CardView)findViewById(R.id.card_news);
        cardFacility = (CardView)findViewById(R.id.card_facility);

        cardPlace.setOnClickListener(this);
        cardNews.setOnClickListener(this);
        cardFacility.setOnClickListener(this);


        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mViewName = (TextView) findViewById(R.id.name);
        mViewIc = (TextView) findViewById(R.id.ic);
        mViewPhone = (TextView) findViewById(R.id. phone);
        mViewEmail = (TextView) findViewById(R.id.email);//
        profileimage = findViewById(R.id.profileImage);

        //FOR Retrieve Information
        myRef.child("Admin").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+dataSnapshot);
                if(dataSnapshot.getValue() != null){
                    Admin admin = dataSnapshot.getValue(Admin.class);
                    //mViewName.setText(admin.name);
                   // mViewIc.setText(admin.ic);
                    //mViewEmail.setText(admin.email);
                    //mViewPhone.setText(admin.phone);
                    //imgurl = admin.getImageUrl();
                    //Picasso.with(getApplicationContext()).load(imgurl).into(profileimage);//using picasso to load image
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //For Update Method
        button = (Button) findViewById(R.id.editBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openUpdateProfile();
            }
        });


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_admin_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("PROFILE ADMIN");


        //Logout
        mLogout = (ImageView) findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainAdmin.this,"Successfully Logout",Toast.LENGTH_SHORT).show();
                logout();
            }
        });

    }



    //Logout Method
    private void logout() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){


            case R.id.card_news: i = new Intent(this,MainNews.class);
                startActivity(i);
                break;



            default:break;
        }
    }
}

