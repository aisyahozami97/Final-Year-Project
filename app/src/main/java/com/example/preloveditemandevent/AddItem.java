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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1 ;
    private EditText mItemName, mItemPrice, mItemDesc, mItemDate, mItemAvailability, mItemCategory;
    private Button mCreateItem;

    private FirebaseAuth mAuth;
    private DatabaseReference mItemRef;
    private ImageView mItemImage;
    final static int Gallery_Pick = 1;
    private ProgressDialog loadingBar;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private Uri mImageUri,CropImageUri;
    private ProgressDialog mProgressBar;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG= "AddItem";
    private Spinner spinnerItemCategory, spinnerItemAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //Declaration for every attribute in xml
        mItemName = (EditText) findViewById(R.id.itemName);
        mItemPrice = (EditText) findViewById(R.id.itemPrice);
        mCreateItem = (Button) findViewById(R.id.createItem);
        mItemImage = (ImageView) findViewById(R.id.imageItem);
        mItemDesc = (EditText) findViewById(R.id.itemDesc);

        mProgressBar = new ProgressDialog(AddItem.this);

        //TOOLBAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.add_item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Next navigation page
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainItem.class));
                finish();
            }
        });


        //Firebase connection
        mAuth = FirebaseAuth.getInstance();
        mItemRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        //Button Create news function
        mCreateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setTitle("Processing");
                mProgressBar.setMessage("Please wait...");
                mProgressBar.show();
                addItem();
            }
        });

        //Load of Image
        mItemImage.setOnClickListener(new View.OnClickListener() {
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
                        AddItem.this,
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
                    .start(AddItem.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            CropImageUri = result.getUri();

            if (resultCode == RESULT_OK) {
                Picasso.get().load(CropImageUri).into(mItemImage);//using picasso to load image
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
    private void createItem(){

        if(CropImageUri !=null){

            final String itemId = mItemRef.push().getKey();
            final StorageReference ref = mStorageRef.child("imageitem").child(itemId + "." +getFileExtension(CropImageUri));
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
                                    //String itemId = mNewsRef.push().getKey();
                                    String itemName = mItemName.getText().toString().trim();
                                    String itemDesc = mItemDesc.getText().toString().trim();
                                    String itemPrice = mItemPrice.getText().toString().trim();
                                    String imgurl = downloadUrl.toString();
                                    String itemdate = String.valueOf(mDisplayDate.getText());
                                    String itemCategory = String.valueOf(spinnerItemCategory.getSelectedItem());
                                    String itemAvailability = String.valueOf(spinnerItemAvailability.getSelectedItem());

                                    ItemAdmin item = new ItemAdmin(itemId,itemName,itemDesc,imgurl, itemCategory, itemAvailability, itemdate, itemPrice);
                                    mItemRef.child("ItemAdmin").child(itemId).setValue(item) //table and primary key
                                            // mNewsRef.child(itemId).setValue(news)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                           if (task.isSuccessful()) {
                                                                               Toast.makeText(AddItem.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                                                                               finish();
                                                                               Intent intent = new Intent(AddItem.this, MainItem.class);
                                                                               intent.putExtra("itemId", itemId);
                                                                               startActivity(intent);
                                                                           } else {
                                                                               Toast.makeText(AddItem.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
    public void addItem()
    {
        String itemName = mItemName.getText().toString().trim();
        String itemDesc = mItemDesc.getText().toString().trim();
        String itemPrice = mItemPrice.getText().toString().trim();


        if (TextUtils.isEmpty(itemName)) {
            Toast.makeText(AddItem.this, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(itemDesc)) {
            Toast.makeText(AddItem.this, "Please enter item description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(itemPrice)) {
            Toast.makeText(AddItem.this, "Please enter item price", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            createItem();
        }
    }

    //Show back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void addListenerOnSpinnerItemSelection() {

        spinnerItemCategory = (Spinner) findViewById(R.id.spinnerItemCategory);
        spinnerItemAvailability = (Spinner) findViewById(R.id.spinnerItemAvailability);


        spinnerItemCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinnerItemAvailability.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    public void addListenerOnButton() {

        mDisplayDate = (TextView) findViewById(R.id.itemDate);
        //spinnerNewsTime = (Spinner) findViewById(R.id.spinnerNewsTime);
        //numVolunteer = (Spinner) findViewById(R.id.numVolunteer);
        //btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }
}
