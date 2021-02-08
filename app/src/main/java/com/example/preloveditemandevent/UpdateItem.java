package com.example.preloveditemandevent;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UpdateItem extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UpdateItem";

    private Toolbar mToolbar;
    private TextView mItemName, mItemPrice, mItemDesc;
    private TextView mItemDate, mItemCategory, mItemAvailability;
    private Button mUpdateButton;
    private ImageView itemimg;

    //private static final String TAG = "NewsAdmin";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myReference;
    private String userID;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Uri mImageUri,CropImageUri;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.update_item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        userID = user.getUid();

        mProgressBar = new ProgressDialog(UpdateItem.this);
        mItemName = (TextView) findViewById(R.id.txtItemName);
        mItemPrice = (TextView) findViewById(R.id.txtItemPrice);
        mItemDesc = (TextView) findViewById(R.id.txtItemDesc);
        mItemDate = (TextView) findViewById(R.id.itemDate);
        mItemCategory = (TextView) findViewById(R.id.tvItemCategory);
        mItemAvailability = (TextView) findViewById(R.id.tvItemAvailability);
        mUpdateButton = findViewById(R.id.btnUpdateItem);
        itemimg = findViewById(R.id.imageItem);

        //to retrive sblom edit
        Intent intent = getIntent();
        final String itemName = intent.getStringExtra("itemName");
        final String itemPrice = intent.getStringExtra("itemPrice");
        final String itemDesc = intent.getStringExtra("itemDesc");
        final String itemCategory = intent.getStringExtra("itemCategory");
        final String itemAvailability = intent.getStringExtra("itemAvailability");
        final String itemDate = intent.getStringExtra("itemDate");
        final String itemId = intent.getStringExtra("itemId");
        final String itemImg = intent.getStringExtra("imgurl");

        mItemName.setText(itemName);
        mItemDesc.setText(itemDesc);
        mItemPrice.setText(itemPrice);
        mItemCategory.setText(itemCategory);
        mItemAvailability.setText(itemAvailability);
        mItemDate.setText(itemDate);

        if(itemImg!=null){
            Picasso.get().load(itemImg).into(itemimg);//using picasso to load image
        }

        mUpdateButton.setOnClickListener(this);
        itemimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

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
                    .start(UpdateItem.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            CropImageUri = result.getUri();

            if (resultCode == RESULT_OK) {
                Picasso.get().load(CropImageUri).into(itemimg);//using picasso to load image
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

    private void updateItem() {
        //update info and image
        if(CropImageUri !=null){

            //final String newsId = mAuth.getCurrentUser().getUid();
            Intent intent = getIntent();
            final String itemId = intent.getStringExtra("itemId");
            final StorageReference ref = mStorageRef.child("imageitem").child(itemId + "." +getFileExtension(mImageUri));

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

                                    String itemName = mItemName.getText().toString().trim();
                                    String itemDesc = mItemDesc.getText().toString().trim();
                                    String itemPrice = mItemPrice.getText().toString().trim();
                                    String itemCategory = mItemCategory.getText().toString().trim();
                                    String itemAvailability = mItemAvailability.getText().toString().trim();
                                    String itemdate = mItemDate.getText().toString().trim();

                                    ItemAdmin item = new ItemAdmin(itemId,itemName,itemDesc, downloadUrl.toString(),itemCategory, itemAvailability, itemdate, itemPrice);
                                    myReference.child("ItemAdmin").child(itemId).setValue(item) //table and primary key
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(UpdateItem.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        //Create baru
                                                        startActivity(new Intent(UpdateItem.this,MainItem.class));
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
            final String itemId= intent.getStringExtra("itemId");
            String itemName = mItemName.getText().toString().trim();
            String itemDesc = mItemDesc.getText().toString().trim();
            String itemPrice = mItemPrice.getText().toString().trim();
            String itemCategory = mItemCategory.getText().toString().trim();
            String itemAvailability = mItemAvailability.getText().toString().trim();
            String itemdate = mItemDate.getText().toString().trim();
            String imgurl = intent.getStringExtra("imgurl");

            ItemAdmin item = new ItemAdmin(itemId,itemName,itemDesc,imgurl, itemCategory, itemAvailability, itemdate, itemPrice);
            myReference.child("ItemAdmin").child(itemId).setValue(item) //table and primary key
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UpdateItem.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                //Create baru
                                startActivity(new Intent(UpdateItem.this,MainItem.class));
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
            updateItem();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if ( mProgressBar!=null && mProgressBar.isShowing() ){
            mProgressBar.cancel();
        }
    }

}
