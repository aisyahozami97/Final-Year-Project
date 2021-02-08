package com.example.preloveditemandevent;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;

import android.os.Bundle;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class JoinEvent extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "JoinEvent";

    private Toolbar mToolbar;
    private TextView mNewsDesc, mNewsName, mNewsLocation, mVolunteer, spinnerNewsTime, spinnerNumVolunteer, mNewsDate, mNewsId;
    private TextView mFirstName, mLastName, mPhone;
    //private Spinner spinnerNewsTime , spinnerNumVolunteer;
    //private EditText mNewsDate;
    private Button mBtnRegVolunteer;
    private ImageView newsimg;

    //private static final String TAG = "NewsAdmin";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth,firebaseAuth;
    private DatabaseReference myReference, userRef, volRef;
    private String userID, eventId;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Uri mImageUri,CropImageUri;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressBar;


    private String authData;



    // @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.join_event_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle("PRELOVED EVENTS");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        userID = user.getUid();

        //IMPORTANT DECLARATION : TO CALL OTHER TABLE DATABASE
        userRef = FirebaseDatabase.getInstance().getReference("User").child("");
        volRef = FirebaseDatabase.getInstance().getReference("SpecificEvent");
        //declaration of date and spinner
        //addListenerOnButton();
        //addListenerOnSpinnerItemSelection();

        mProgressBar = new ProgressDialog(JoinEvent.this);

        //Declaration for every attribute in layout
        //ada kaitan dgn retrive call #
        mNewsDesc = findViewById(R.id.newsDesc);
        mNewsName =  findViewById(R.id.newsName);
        mNewsId =  findViewById(R.id.newsId);
        mNewsLocation = findViewById(R.id.newsLocation);
        mNewsDate = findViewById(R.id.newsDate);
        spinnerNewsTime = findViewById(R.id.spinnerNewsTime);
        spinnerNumVolunteer = findViewById(R.id.numVolunteer);

        //User Details
        mFirstName = findViewById(R.id.vfirstname);
        mLastName = findViewById(R.id.vlastname);
        mPhone = findViewById(R.id.vphone);

        //IMPORTANT : TO CALL OTHER TABLE DATABASE
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //call database
                User data = dataSnapshot.child(userID).getValue(User.class);

                mFirstName.setText(data.firstName);
                mLastName.setText(data.lastName);
                mPhone.setText(data.phoneNo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //***mNewsDate = (EditText) findViewById(R.id.newsDate);
        //spinnerNewsTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        //spinnerNumVolunteer = (Spinner) findViewById(R.id.numVolunteer);

        mBtnRegVolunteer = findViewById(R.id.btnRegVolunteer);

        newsimg = findViewById(R.id.image);
        //to retrive sblom edit
        Intent intent = getIntent();
        final String newsDesc = intent.getStringExtra("news_desc");
        final String newsId = intent.getStringExtra("newsId");
        final String newsimgurl = intent.getStringExtra("imgurl");
        final String newsName = intent.getStringExtra("newsName");
        final String newsLocation = intent.getStringExtra("newsLocation");
        final String newsDate = intent.getStringExtra("newsDate");
        final String newsTime = intent.getStringExtra("newsTime");
        final String numVolunteer = intent.getStringExtra("numVolunteer");

        final String firstname = intent.getStringExtra("firstname");
        final String lastname = intent.getStringExtra("lastname");
        final String phone = intent.getStringExtra("phone");

        //Toast.makeText(this, "Volunteer " + numVolunteer, Toast.LENGTH_LONG).show();

        //mNewsDate.setText(newsDate);
        //spinnerNewsTime.setText(newsTime);
        //spinnerNumVolunteer.setText(numVolunteer);
        //String newsTime = spinnerNewsTime.getSelectedItem().toString();
//        String numVolunteer = spinnerNumVolunteer.getSelectedItem().toString();

        if(newsimgurl!=null){
            Picasso.get().load(newsimgurl).into(newsimg);//using picasso to load image
            //newsName.setText(intent.getNewsName());
            //retrieve call #
            mNewsId.setText(newsId);
            mNewsName.setText(newsName);
            mNewsDesc.setText(newsDesc);
            mNewsDate.setText(newsDate);
            mNewsLocation.setText(newsLocation);
            spinnerNewsTime.setText(newsTime);
            spinnerNumVolunteer.setText(numVolunteer);

            mFirstName.setText(firstname);
            mLastName.setText(lastname);
            mPhone.setText(phone);
        }


        mBtnRegVolunteer.setOnClickListener(this);

    }
    //to get extension for our file
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //show back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }




    @Override
    public void onClick(View v) {
        if (v == mBtnRegVolunteer) {

            mProgressBar.setTitle("Processing");
            mProgressBar.setMessage("Please wait...");
            mProgressBar.show();
            //joinEvent();

            //to get current user
            firebaseAuth = FirebaseAuth.getInstance();

            String id = volRef.push().getKey(); // push existing id event
            String userid = firebaseAuth.getCurrentUser().getUid();   //to get current user
            //String eventId = firebaseAuth.getCurrentUser().getUid();//currentevent
            String firstname = mFirstName.getText().toString().trim();
            String lastname = mLastName.getText().toString().trim();
            String phone = mPhone.getText().toString().trim();
            String newsname = mNewsName.getText().toString().trim();
            String newsid = mNewsId.getText().toString().trim();
            //String status = "pending";
            //String message = "waiting feedback";
            Log.i("names", id);
            SpecificEvent specificEvent = new SpecificEvent(userid,id,newsid,firstname,lastname,phone,newsname);

            //volRef.child(id).setValue(specificEvent);
            volRef.child(newsid).child(userid).setValue(specificEvent);
                Toast.makeText(this, "Volunteer Registered", Toast.LENGTH_LONG).show();
            //nak paggil kat page lain
            Intent myIntent = new Intent(JoinEvent.this, MainNewsUser.class);
            myIntent.putExtra("id", id);
            myIntent.putExtra("firstname", String.valueOf(mFirstName));
            myIntent.putExtra("lastname", String.valueOf(mLastName));
            myIntent.putExtra("phone", String.valueOf(mPhone));
            myIntent.putExtra("userid", userid);
            myIntent.putExtra("eventname", String.valueOf(mNewsName));

            startActivity(myIntent);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if ( mProgressBar!=null && mProgressBar.isShowing() ){
            mProgressBar.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(JoinEvent.this, MainNewsUser.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
