package com.example.preloveditemandevent;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddNews extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1 ;
    private EditText mNewsDesc;
    private EditText mNewsName;
    private EditText mNewsLocation;
    private String authData;
    private ImageView mNewsImage;
    //private Date mNewsDate;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Spinner spinnerNewsTime, numVolunteer;
    private Button mCreateNews;

    private FirebaseAuth mAuth;
    private DatabaseReference mNewsRef;

    final static int Gallery_Pick = 1;
    private ProgressDialog loadingBar;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private Uri mImageUri,CropImageUri;
    private ProgressDialog mProgressBar;
    private static final String TAG= "AddNews";


    @Override
    protected void onStop() {
        super.onStop();
        if ( mProgressBar!=null && mProgressBar.isShowing() ){
            mProgressBar.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        //Declaration for every attribute in xml
        mNewsDesc = (EditText) findViewById(R.id.news_desc);
        mNewsName = (EditText) findViewById(R.id.news_name);
        mNewsLocation = (EditText) findViewById(R.id.news_location);
        mNewsImage = (ImageView) findViewById(R.id.imageNews);
        //mNewsDate = (Date) findViewById(R.id.newsDate);

        mCreateNews = (Button) findViewById(R.id.createNews);
        mProgressBar = new ProgressDialog(AddNews.this);



        //TOOLBAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.add_news_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Next navigation page
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainNews.class));
                finish();
            }
        });


        //Firebase connection
        mAuth = FirebaseAuth.getInstance();

        mNewsRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        //Button Create news function
        mCreateNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String mdate = String.valueOf(mDisplayDate.getText());
                //String mtime = String.valueOf(spinnerNewsTime.getSelectedItem());
                //String mnumvolunteer = String.valueOf(numVolunteer.getSelectedItem());

                // if ( !TextUtils.isEmpty(mdate) && !TextUtils.isEmpty(mtime) && !TextUtils.isEmpty(mnumvolunteer) ) {

                mProgressBar.setTitle("Processing");
                mProgressBar.setMessage("Please wait...");

                mProgressBar.show();
                addNews();

            }
        });



        //Load of Image
        mNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        //Date
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddNews.this,
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

    //Image Load using Picasse and centerCrop
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(AddNews.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            CropImageUri = result.getUri();

            if (resultCode == RESULT_OK) {
                Picasso.get().load(CropImageUri).into(mNewsImage);//using picasso to load image
                //Picasso.with(getApplicationContext()).load(CropImageUri).into(mNewsImage);//using picasso to load image
            }
        }

    }

    //to get extension for the file
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Upload image method
    private void createNews(){

        if(CropImageUri !=null){

            final String newsId = mNewsRef.push().getKey();
            final StorageReference ref = mStorageRef.child("images").child(newsId + "." +getFileExtension(CropImageUri));
            //final StorageReference ref = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(CropImageUri));

            mUploadTask = ref.putFile(CropImageUri)//save image into storage reference
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot task) {

                            Toast.makeText(getApplicationContext(),"Upload Successful",Toast.LENGTH_LONG).show();
                            //get url from the storage reference and assign to uri
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    //Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();
                                    //String newsId = mNewsRef.push().getKey();
                                    String newsDesc = mNewsDesc.getText().toString().trim();
                                    String newsName = mNewsName.getText().toString().trim();
                                    String newsLocation = mNewsLocation.getText().toString().trim();
                                    String mdate = String.valueOf(mDisplayDate.getText());
                                    String mtime = String.valueOf(spinnerNewsTime.getSelectedItem());
                                    String mnumvolunteer = String.valueOf(numVolunteer.getSelectedItem());

                                    //String newsDate = .getText().toString().trim();
                                    //String newsTime = .getText().toString().trim();
                                    //String numVolunteer = .getText().toString().trim();

                                    String imgurl = downloadUrl.toString();

                                    NewsAdmin news = new NewsAdmin(newsId,newsDesc,imgurl,newsName, newsLocation, mdate , mtime, mnumvolunteer);
                                    mNewsRef.child("NewsAdmin").child(newsId).setValue(news) //table and primary key
                                            // mNewsRef.child(newsId).setValue(news)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                           if (task.isSuccessful()) {
                                                                               Toast.makeText(AddNews.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                                                                               finish();
                                                                               Intent intent = new Intent(AddNews.this, MainNews.class);
                                                                               intent.putExtra("newsId", newsId);
                                                                               startActivity(intent);
                                                                           } else {
                                                                               Toast.makeText(AddNews.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                           }
                                                                       }
                                                                   }
                                            );
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

        }else{
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }





    //Ensure that the attribute is not null
    public void addNews()
    {
        String newsName = mNewsName.getText().toString().trim();
        String newsDesc = mNewsDesc.getText().toString().trim();
        String newsLocation = mNewsLocation.getText().toString().trim();

        if (TextUtils.isEmpty(newsName)) {
            Toast.makeText(AddNews.this, "Please enter news name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newsDesc)) {
            Toast.makeText(AddNews.this, "Please enter news description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newsLocation)) {
            Toast.makeText(AddNews.this, "Please enter news location", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            createNews();
        }
    }

    //Show back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void addListenerOnSpinnerItemSelection() {

        spinnerNewsTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        numVolunteer = (Spinner) findViewById(R.id.numVolunteer);


        spinnerNewsTime.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        numVolunteer.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    public void addListenerOnButton() {


        mDisplayDate = (TextView) findViewById(R.id.newsDate);
        spinnerNewsTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        numVolunteer = (Spinner) findViewById(R.id.numVolunteer);
        //btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }
}
