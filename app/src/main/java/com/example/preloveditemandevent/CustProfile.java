package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class CustProfile extends AppCompatActivity {

    TextView username,lastname,firstname,phone,email, address;
    Button btn_update;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //retrieve database
        ref = FirebaseDatabase.getInstance().getReference("User").child("");
        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getUid();

        username = (TextView)findViewById(R.id.username);
        lastname = (TextView)findViewById(R.id.lastname);
        firstname = (TextView)findViewById(R.id.firstname);
        phone = (TextView)findViewById(R.id.phone);
        email = (TextView)findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);

        //TOOLBAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.cust_profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Next navigation page
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //call database
                User data = dataSnapshot.child(userId).getValue(User.class);

                username.setText(data.userName);
                lastname.setText(data.lastName);
                firstname.setText(data.firstName);
                phone.setText(data.phoneNo);
                email.setText(data.email);
                address.setText(data.address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //onCLick btnUpdate
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call child in user by userId
                ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //call each child
                        Log.i("test", lastname.getText().toString());
                        dataSnapshot.getRef().child("lastName").setValue(lastname.getText().toString());
                        dataSnapshot.getRef().child("firstName").setValue(firstname.getText().toString());
                        dataSnapshot.getRef().child("phoneNo").setValue(phone.getText().toString());
                        dataSnapshot.getRef().child("userName").setValue(username.getText().toString());
                        dataSnapshot.getRef().child("address").setValue(address.getText().toString());

                        Intent intent = new Intent(CustProfile.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });
            }
        });


    }
}
