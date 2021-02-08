package com.example.preloveditemandevent;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UpdateNews extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UpdateNews";

    private Toolbar mToolbar;
    private TextView mNewsDesc, mNewsName, mNewsLocation, mVolunteer;
    private Spinner spinnerNewsTime , spinnerNumVolunteer;
    private EditText mNewsDate;
    private Button mUpdateButton, mCancelButton;
    private ImageView newsimg;

    //private static final String TAG = "NewsAdmin";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myReference;
    private String userID;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Uri mImageUri,CropImageUri;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressBar;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_news);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.update_news_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        userID = user.getUid();

        //declaration of date and spinner
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        mProgressBar = new ProgressDialog(UpdateNews.this);

        //Declaration for every attribute in layput
        mNewsDesc = (TextView) findViewById(R.id.txtNews);
        mNewsName = (TextView) findViewById(R.id.txtNewsName);
        mNewsLocation = (TextView) findViewById(R.id.txtNewsLocation);

        //***mNewsDate = (EditText) findViewById(R.id.newsDate);
        spinnerNewsTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        spinnerNumVolunteer = (Spinner) findViewById(R.id.numVolunteer);

        mUpdateButton = findViewById(R.id.btnUpdateNews);
        mCancelButton = findViewById(R.id.btn_cancel);
        newsimg = findViewById(R.id.image);
        //to retrive sblom edit
        Intent intent = getIntent();
        final String newsDesc = intent.getStringExtra("news_desc");
        final String newsId = intent.getStringExtra("newsId");
        final String newsimgurl = intent.getStringExtra("imgurl");
        final String newsName = intent.getStringExtra("newsName");
        final String newsLocation = intent.getStringExtra("newsLocation");
        //final String newsDate = intent.getStringExtra("newsDate");
        //final String newsTime = intent.getStringExtra("newsTime");
        final String numVolunteer = intent.getStringExtra("numVolunteer");

        Toast.makeText(this, "Volunteer " + numVolunteer, Toast.LENGTH_LONG).show();

        mNewsDesc.setText(newsDesc);
        mNewsName.setText(newsName);
        mNewsLocation.setText(newsLocation);
        //mNewsDate.setText(newsDate);
        //spinnerNewsTime.setText(newsTime);
        //spinnerNumVolunteer.setText(numVolunteer);
        String newsTime = spinnerNewsTime.getSelectedItem().toString();
//        String numVolunteer = spinnerNumVolunteer.getSelectedItem().toString();

        if(newsimgurl!=null){
            Picasso.get().load(newsimgurl).into(newsimg);//using picasso to load image
        }

        mUpdateButton.setOnClickListener(this);
        newsimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(UpdateNews.this, MainNews.class));
                // finish();
                //startActivity(new Intent(getApplicationContext(), MainEventsRv.class));
            }
        });


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateNews.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker datePicker, int year , int month, int day){
                month = month + 1;

                Log.d(TAG,"onDataSet: mm/dd/yyy:"+month+"/"+day+"/"+year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null)
        {

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            CropImageUri = result.getUri();

            if (resultCode == RESULT_OK) {
                Picasso.get().load(CropImageUri).into(newsimg);//using picasso to load image
            }
        }

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

    private void updateNews() {
        //update info and image
        if(CropImageUri !=null){

            //final String newsId = mAuth.getCurrentUser().getUid();
            Intent intent = getIntent();
            final String newsId= intent.getStringExtra("newsId");
            final StorageReference ref = mStorageRef.child("images").child(newsId + "." +getFileExtension(mImageUri));

            ref.putFile(CropImageUri)//save image into storage reference
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot task) {

                            Toast.makeText(getApplicationContext(),"Upload Successful",Toast.LENGTH_LONG).show();
                            //get url from the storage reference and assign to uri
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    String newsDesc = mNewsDesc.getText().toString().trim();
                                    String newsName = mNewsName.getText().toString().trim();
                                    String newsLocation = mNewsLocation.getText().toString().trim();
                                    String newsDate = String.valueOf(mDisplayDate.getText());
                                    //SpinnerAdapter newsTime = mNewsTime.getAdapter();
                                    //SpinnerAdapter numVolunteer = mNewsTime.getAdapter();
                                    String newsTime = String.valueOf(spinnerNewsTime.getSelectedItem());
                                    String numVolunteer = String.valueOf(spinnerNumVolunteer.getSelectedItem());

                                    NewsAdmin news = new NewsAdmin(newsId,newsDesc,downloadUrl.toString(), newsName, newsLocation, newsDate , newsTime, numVolunteer);
                                    myReference.child("NewsAdmin").child(newsId).setValue(news) //table and primary key
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(UpdateNews.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        //Create baru
                                                        startActivity(new Intent(UpdateNews.this,MainNews.class));
                                                    }
                                                }
                                            });

                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        }

                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });

        } else{ //when user dont want to change their pic, update only the info and use the same imageurl
            Intent intent = getIntent();
            final String newsId= intent.getStringExtra("newsId");
            String newsDesc = mNewsDesc.getText().toString().trim();
            String imgurl = intent.getStringExtra("imgurl");
            String newsName = mNewsName.getText().toString().trim();
            String newsLocation = mNewsLocation.getText().toString().trim();
            String newsDate = String.valueOf(mDisplayDate.getText());
            //SpinnerAdapter newsTime = mNewsTime.getAdapter();
            //SpinnerAdapter numVolunteer = mNewsTime.getAdapter();
            String newsTime = String.valueOf(spinnerNewsTime.getSelectedItem());
            String numVolunteer = String.valueOf(spinnerNumVolunteer.getSelectedItem());

            NewsAdmin news = new NewsAdmin(newsId,newsDesc,imgurl, newsName, newsLocation, newsDate, newsTime, numVolunteer);
            myReference.child("NewsAdmin").child(newsId).setValue(news) //table and primary key
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UpdateNews.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                //Create baru
                                startActivity(new Intent(UpdateNews.this,MainNews.class));
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mUpdateButton) {
            mProgressBar.setTitle("Processing");
            mProgressBar.setMessage("Please wait...");
            mProgressBar.show();
            updateNews();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if ( mProgressBar!=null && mProgressBar.isShowing() ){
            mProgressBar.cancel();
        }
    }

    public void addListenerOnSpinnerItemSelection() {

        spinnerNewsTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        spinnerNumVolunteer = (Spinner) findViewById(R.id.numVolunteer);


        spinnerNewsTime.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinnerNumVolunteer.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }



    public void addListenerOnButton() {


        mDisplayDate = (TextView) findViewById(R.id.newsDate);
        spinnerNewsTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        spinnerNumVolunteer = (Spinner) findViewById(R.id.numVolunteer);
        //btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }

}
