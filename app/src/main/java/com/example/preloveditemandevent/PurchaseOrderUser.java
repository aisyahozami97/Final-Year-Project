package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class PurchaseOrderUser extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference cartRef, userRef, purchaseOrderRef;
    ArrayList<PurchaseOrder> listCart;
    RecyclerView recyclerViewUser;
    SearchView searchViewUser;
    private Toolbar mToolbar;
    private TextView mFirstName, mLastName, mPhone, mAddress, mTotalPrice , mStatusPurchase;
    private String userID;
    private StorageReference mStorageRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth,firebaseAuth;
    private Button mBtnMakePayment, mBtnDonePayment, mBtnCalcPrice;
    Double totalPrice= 0.0;
    String mCartItemName, cartItemDesc, cartItemPrice,cartItemAvailability,cartTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_user);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        final String userId = mAuth.getCurrentUser().getUid();

        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        userRef = FirebaseDatabase.getInstance().getReference("User");
        purchaseOrderRef = FirebaseDatabase.getInstance().getReference("PurchaseOrder").child(userId).child("");



        recyclerViewUser = findViewById(R.id.rvCart);
        searchViewUser = findViewById(R.id.searchViewCart);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.cartList_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Purchase Order");

        //User Details
        mFirstName = findViewById(R.id.txtUserFirstName);
        mLastName = findViewById(R.id.txtUserLastName);
        mPhone = findViewById(R.id.txtUserPhone);
        mAddress = findViewById(R.id.txtUserAddress);

        mTotalPrice = findViewById(R.id.txtNewTotalPrice);
        mStatusPurchase = findViewById(R.id.statusPurchaseOrder);

        //IMPORTANT : TO CALL OTHER TABLE DATABASE
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //call database
                User data = dataSnapshot.child(userId).getValue(User.class);

                mFirstName.setText(data.firstName);
                mLastName.setText(data.lastName);
                mPhone.setText(data.phoneNo);
                mAddress.setText(data.address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        purchaseOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //call database
                //Intent intent = getIntent();
                //final String orderId = intent.getStringExtra("orderId");
                //PurchaseOrder data = dataSnapshot.child(userID).child(orderId).child("orderStatus").getValue(PurchaseOrder.class);

                //PurchaseOrder data = dataSnapshot.child(userId).child("orderId").child("totalPrice").getValue(PurchaseOrder.class);
                //PurchaseOrder data2 = dataSnapshot.child(userId).child("orderId").child("orderStatus").getValue(PurchaseOrder.class);
                //mTotalPrice.setText(data.totalPrice);
                //mStatusPurchase.setText(data2.orderStatus);

                //DISPLAY CURRENT FROM PURCHASE ORDER/UserID/OrderId/ totalprice
                if(dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        PurchaseOrder message = ds.getValue(PurchaseOrder.class);

                        String totalPrice = message.getTotalPrice();
                        String orderStatus = message.getOrderStatus();

                        mTotalPrice.setText(totalPrice);
                        mStatusPurchase.setText(orderStatus);

                    }
                }

                //HANYA KELUAR LINK
                //DatabaseReference data = purchaseOrderRef.child("orderStatus");
                //mTotalPrice.setText(data.toString());
                //mStatusPurchase.setText(data.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mBtnMakePayment = findViewById(R.id.btnMakePayment);
        //mBtnCalcPrice = findViewById(R.id.btnCalculate);

        mBtnDonePayment = findViewById(R.id.btnDonePayment);

        Intent intent = getIntent();
        final String firstname = intent.getStringExtra("firstname");
        final String lastname = intent.getStringExtra("lastname");
        final String phone = intent.getStringExtra("phone");
        final String address = intent.getStringExtra("address");

        mFirstName.setText(firstname);
        mLastName.setText(lastname);
        mPhone.setText(phone);
        mAddress.setText(address);

        mBtnMakePayment.setOnClickListener(this);
        mBtnDonePayment.setOnClickListener(this);



    }


    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(PurchaseOrderUser.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        if (v == mBtnMakePayment) {
            //userId,  orderId,  itemId,  custFirstName,  custLastName,  custPhone,  custAddress
            //orderItemName,  orderItemDesc,  orderItemPrice,  orderItemAvailability,  orderTotalPrice,
            //orderStatus,  orderDate,  paymentReceipt
            //to get current user

            Toast.makeText(this, "Direct to make payment", Toast.LENGTH_LONG).show();
            //nak paggil kat page lain
            Intent myIntent = new Intent(PurchaseOrderUser.this, PaymentReceipt.class);
            startActivity(myIntent);
        }

        else if (v == mBtnDonePayment){
            Toast.makeText(this, "Direct to homepage", Toast.LENGTH_LONG).show();
            //nak paggil kat page lain
            Intent myIntent = new Intent(PurchaseOrderUser.this, MainActivity.class);
            startActivity(myIntent);
        }
    }


}