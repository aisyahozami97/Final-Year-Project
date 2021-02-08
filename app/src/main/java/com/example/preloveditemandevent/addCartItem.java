package com.example.preloveditemandevent;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class addCartItem extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UpdateItem";

    private Toolbar mToolbar;
    private TextView mItemName, mItemPrice, mItemDesc, mItemId;
    private TextView mItemDate;
    private TextView mItemCategory;
    private TextView mItemAvailability;
    private String totalPrice = String.valueOf(0);
    private Button mAddCartButton;
    private ImageView itemimg;

    //private static final String TAG = "NewsAdmin";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth,firebaseAuth;
    private DatabaseReference myReference, cartRef, itemRef;
    private String userID;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Uri mImageUri,CropImageUri;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cart_item);

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

        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        itemRef = FirebaseDatabase.getInstance().getReference("ItemAdmin").child("itemId").push();


        mProgressBar = new ProgressDialog(addCartItem.this);
        mItemName = (TextView) findViewById(R.id.txtItemName);
        mItemPrice = (TextView) findViewById(R.id.txtItemPrice);
        mItemDesc = (TextView) findViewById(R.id.txtItemDesc);
        mItemDate = (TextView) findViewById(R.id.itemDate);
        mItemCategory = (TextView) findViewById(R.id.tvItemCategory);
        mItemAvailability = (TextView) findViewById(R.id.tvItemAvailability);
        mAddCartButton = findViewById(R.id.btnAddCart);
        itemimg = findViewById(R.id.imageItem);

        //to retrive sblom edit
        Intent intent = getIntent();
        final String itemName = intent.getStringExtra("itemName");
        final String itemPrice = intent.getStringExtra("itemPrice");
        final String itemDesc = intent.getStringExtra("itemDesc");
        final String itemCategory = intent.getStringExtra("itemCategory");
        final String itemAvailability = intent.getStringExtra("itemAvailability");
        final String itemDate = intent.getStringExtra("itemDate");
       // final String itemId = String.valueOf(intent.putExtra("itemId", String.valueOf(itemRef.push())));
        final String itemImg = intent.getStringExtra("imgurl");

        mItemName.setText(itemName);
        mItemDesc.setText(itemDesc);
        mItemPrice.setText(itemPrice);
        mItemCategory.setText(itemCategory);
        mItemAvailability.setText(itemAvailability);
        mItemDate.setText(itemDate);
        //mItemId.setText(itemId);

        if(itemImg!=null){
            Picasso.get().load(itemImg).into(itemimg);//using picasso to load image
        }

        mAddCartButton.setOnClickListener(this);
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
                    .start(addCartItem.this);
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


    @Override
    public void onClick(View v) {
        if (v == mAddCartButton) {
            //GET VALUE AND COMPARE STRING
            String availability = mItemAvailability.getText().toString();
            //addcart();
            if( availability.equalsIgnoreCase("AVAILABLE") )
            {//to get current user
                mProgressBar.setTitle("Processing");
                mProgressBar.setMessage("Please wait...");
                mProgressBar.show();
            firebaseAuth = FirebaseAuth.getInstance();

            String cartId = cartRef.push().getKey(); // push existing id event
            String userId = firebaseAuth.getCurrentUser().getUid();   //to get current user
            //String itemId = String.valueOf(itemRef.push().setValue(userID));  //to get current item
               //String itemId =  itemRef.push()
                //String itemId = String.valueOf(myReference.push().setValue(mI));//currentevent
                //String itemId = cartitemid.getText().toString().trim();
                String cartitemid = itemRef.getKey();
                Log.i("itemId", cartitemid);
            String itemName = mItemName.getText().toString().trim();
            String itemDesc = mItemDesc.getText().toString().trim();
            String itemPrice = mItemPrice.getText().toString().trim();
            String itemAvailability = mItemAvailability.getText().toString().trim();
            String totalPrice = String.valueOf(0);
            //String status = "pending";
            //String message = "waiting feedback";
            Log.i("names", cartId);
            CartItem cartItem = new CartItem(cartId,userId,cartitemid, itemName, itemDesc, itemPrice, itemAvailability, totalPrice);

            //volRef.child(id).setValue(specificEvent);
            cartRef.child(userId).child(cartitemid).setValue(cartItem);
            Toast.makeText(this, "Successfully add to cart", Toast.LENGTH_LONG).show();
            //nak paggil kat page lain
            Intent myIntent = new Intent(addCartItem.this, MainItemUser.class);
            myIntent.putExtra("cartId", cartId);
            myIntent.putExtra("itemName", String.valueOf(mItemName));
            myIntent.putExtra("itemDesc", String.valueOf(mItemDesc));
            myIntent.putExtra("itemPrice", String.valueOf(mItemPrice));
            myIntent.putExtra("itemAvailability", String.valueOf(mItemAvailability));
            myIntent.putExtra("totalPrice",totalPrice);
            myIntent.putExtra("userId", userId);

            startActivity(myIntent);
            }
            else if( availability.equalsIgnoreCase("SOLD OUT") ) {
                Toast.makeText(this, "Sorry this item has Sold Out. Unable to add to cart.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(addCartItem.this, MainItemUser.class));
            }
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
