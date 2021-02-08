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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PaymentReceipt extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1 ;
    //private EditText mItemName, mItemPrice, mItemDesc, mItemDate, mItemAvailability, mItemCategory;
    private Button mBtnUpload, mBtnLater;

    private FirebaseAuth mAuth;
    private DatabaseReference mReceiptRef, mPurchaseOrderRef;
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
        setContentView(R.layout.activity_payment_receipt);

        //Declaration for every attribute in xml
        mItemImage = (ImageView) findViewById(R.id.imageItem);
        mBtnUpload = (Button) findViewById(R.id.btnUploadReceipt);
        mBtnLater = (Button) findViewById(R.id.btnLater);

        mProgressBar = new ProgressDialog(PaymentReceipt.this);

        //TOOLBAR
        Toolbar mToolbar = (Toolbar) findViewById(R.id.payment_item_toolbar);
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


        //Firebase connection
        mAuth = FirebaseAuth.getInstance();
        mReceiptRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        String userId = mAuth.getCurrentUser().getUid();
        //UPDATE PURCHASEORDE/USERID/ORDERID/PAYMENTRECEIPT
        mPurchaseOrderRef = FirebaseDatabase.getInstance().getReference().child("PurchaseOrder").child(userId).child("");


        //Button Create news function
        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setTitle("Processing");
                mProgressBar.setMessage("Please wait...");
                mProgressBar.show();
                uploadReceipt();
            }
        });

        mBtnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentReceipt.this,PurchaseOrderUser.class));
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
                    .start(PaymentReceipt.this);
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
    private void uploadReceipt(){

        if(CropImageUri !=null){

            //UPDATE PURCHASEORDE/USERID/ORDERID/PAYMENTRECEIPT
            Intent intent = getIntent();
            final String orderId = intent.getStringExtra("orderId");
            //final String receiptId = mReceiptRef.push().getKey();
            final StorageReference ref = mStorageRef.child("imageReceipt").child(orderId + "." +getFileExtension(CropImageUri));
            //final StorageReference ref = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(CropImageUri));

            mUploadTask = ref.putFile(CropImageUri)//save image into storage reference
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot task) {

                            Toast.makeText(getApplicationContext(),"Payment Successful",Toast.LENGTH_LONG).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    //UPDATE PURCHASEORDE/USERID/ORDERID/PAYMENTRECEIPT
                                    mPurchaseOrderRef.child(orderId).child("paymentReceipt").setValue(downloadUrl.toString());
                                    mPurchaseOrderRef.child(orderId).child("orderStatus").setValue("Paid");
                                    finish();
                                    Intent intent = new Intent(PaymentReceipt.this, PurchaseOrderUser.class);
                                    startActivity(intent);
                                    //get url from the storage reference and assign to uri
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

    //Show back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
