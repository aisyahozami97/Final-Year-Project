package com.example.preloveditemandevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegisterVolunteer extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "JoinEvent";

    private Toolbar mToolbar;
    private TextView mDisplayEventName, mDisplayFirstName, mDisplayLastName, mDisplayPhone;
    private Button mBtnRegVolunteer;

    //private static final String TAG = "NewsAdmin";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myReference, userRef;
    private String userID, newsId;

    private StorageReference mStorageRef;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registervolunteer);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.register_volunteer_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //untuk event
        myReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        userID = user.getUid();
        newsId = user.getUid();

        //IMPORTANT DECLARATION : TO CALL OTHER TABLE DATABASE
        userRef = FirebaseDatabase.getInstance().getReference("User").child("");

        mProgressBar = new ProgressDialog(RegisterVolunteer.this);

        //Declaration for every attribute in layout
        //ada kaitan dgn retrive call #
        mDisplayEventName = (TextView) findViewById(R.id.displayNewsName);
        //User Details
        mDisplayFirstName = (TextView) findViewById(R.id.displayfirstname);
        mDisplayLastName = (TextView) findViewById(R.id.displaylastname);
        mDisplayPhone = (TextView) findViewById(R.id.displayphone);

        //IMPORTANT : TO CALL OTHER TABLE DATABASE
        Intent intent = getIntent();
        final String newsName = intent.getStringExtra("newsName");

        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //call database
                //NewsAdmin data2 = dataSnapshot.child(newsId).getValue(NewsAdmin.class);
                mDisplayEventName.setText(newsName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //IMPORTANT : TO CALL OTHER TABLE DATABASE
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //call database
                User data = dataSnapshot.child(userID).getValue(User.class);
                //mDisplayEventName.setText(newsName);
                mDisplayFirstName.setText(data.firstName);
                mDisplayLastName.setText(data.lastName);
                mDisplayPhone.setText(data.phoneNo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
