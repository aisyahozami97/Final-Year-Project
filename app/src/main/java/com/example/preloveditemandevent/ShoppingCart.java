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

public class ShoppingCart extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference cartRef, userRef, purchaseOrderRef;
    ArrayList<CartItem> listCart;
    RecyclerView recyclerViewUser;
    SearchView searchViewUser;
    private Toolbar mToolbar;
    private TextView mFirstName, mLastName, mPhone, mAddress, mTotalPrice;
    private String userID;
    private StorageReference mStorageRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth,firebaseAuth;
    private Button mBtnCheckOut, mBtnCalcPrice;
    //Double totalPrice= 0.0;
    Double totalPrice;
    String mCartItemName, cartItemDesc, cartItemPrice,cartItemAvailability,cartTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        userRef = FirebaseDatabase.getInstance().getReference("User");
        purchaseOrderRef = FirebaseDatabase.getInstance().getReference("PurchaseOrder");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        recyclerViewUser = findViewById(R.id.rvCart);
        searchViewUser = findViewById(R.id.searchViewCart);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.cartList_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CART");

        //User Details
        mFirstName = findViewById(R.id.txtUserFirstName);
        mLastName = findViewById(R.id.txtUserLastName);
        mPhone = findViewById(R.id.txtUserPhone);
        mAddress = findViewById(R.id.txtUserAddress);
        mTotalPrice = findViewById(R.id.txtNewTotalPrice);

        //IMPORTANT : TO CALL OTHER TABLE DATABASE
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //call database
                User data = dataSnapshot.child(userID).getValue(User.class);

                mFirstName.setText(data.firstName);
                mLastName.setText(data.lastName);
                mPhone.setText(data.phoneNo);
                mAddress.setText(data.address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mBtnCheckOut = findViewById(R.id.btnCheckOut);
        mBtnCalcPrice = findViewById(R.id.btnCalculate);


        Intent intent = getIntent();
        final String firstname = intent.getStringExtra("firstname");
        final String lastname = intent.getStringExtra("lastname");
        final String phone = intent.getStringExtra("phone");
        final String address = intent.getStringExtra("address");

        mFirstName.setText(firstname);
        mLastName.setText(lastname);
        mPhone.setText(phone);
        mAddress.setText(address);


        mBtnCheckOut.setOnClickListener(this);

        mBtnCalcPrice.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                    totalPrice= 0.0;
                for(int i  = 0; i < listCart.size(); i++) {
                    //Double totalPrice= 0.0;
                    //CHANGE STRING TO DOUBLE
                    Double itemPrice = Double.parseDouble(listCart.get(i).getCartItemPrice());
                    totalPrice += itemPrice;
                    //totalPrice += itemPrice + 5;

                    //totalPrice += curPrice;
                }
                //Add Postage
                totalPrice += 5 ;
                String newTotalPrice = String.valueOf(totalPrice);
                mTotalPrice.setText(newTotalPrice);
            }
        });

    }




    @Override
    protected void onStart() {
        //TO GET ONLY CURRENT USER CHILDREN IN DATABASE
        firebaseAuth = FirebaseAuth.getInstance();
        String userid = firebaseAuth.getCurrentUser().getUid();
        super.onStart();
        if(cartRef != null){
            //TO GET ONLY CURRENT USER CHILDREN IN DATABASE
            cartRef.child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        listCart = new ArrayList<>();
                        //double currentPrice = 0;
                        //RETRIEVE FROM MAIN ROOT TO ANOTHER CHILDREN

                            for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                CartItem message = ds.getValue(CartItem.class);
                                CartItem messageSet = new CartItem();
                                String userId = message.getUserId();
                                String cartId = message.getCartId();
                                String itemId = message.getItemId();
                                String cartItemName = message.getCartItemName();
                                String cartItemDesc = message.getCartItemDesc();
                                String cartItemPrice = message.getCartItemPrice();
                                String cartItemAvailability = message.getCartItemAvailability();
                                //String cartTotalPrice = currentPrice + message.getCartTotalPrice();
                                String cartTotalPrice = message.getCartTotalPrice();

                                CartItem fire = new CartItem(userId, cartId, itemId, cartItemName, cartItemDesc, cartItemPrice, cartItemAvailability, cartTotalPrice);
                                listCart.add(fire);
                            }




                        //TextView productPriceTextView = (TextView) findViewById(R.id.TextViewSubtotal);
                        //productPriceTextView.setText("Subtotal: $" + subTotal);


                    }
                    ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(listCart);
                    recyclerViewUser.setAdapter(shoppingCartAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ShoppingCart.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        if(searchViewUser != null){
            searchViewUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String str){

        ArrayList<CartItem> myList = new ArrayList<>();
        for (CartItem object : listCart)
        {
            if(object.cartItemName.toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(myList);
        recyclerViewUser.setAdapter(shoppingCartAdapter);
    }

    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(ShoppingCart.this, ItemCategory.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        if (v == mBtnCheckOut) {
            //userId,  orderId,  itemId,  custFirstName,  custLastName,  custPhone,  custAddress
            //orderItemName,  orderItemDesc,  orderItemPrice,  orderItemAvailability,  orderTotalPrice,
            //orderStatus,  orderDate,  paymentReceipt
            //to get current user


            firebaseAuth = FirebaseAuth.getInstance();

            String orderId = purchaseOrderRef.push().getKey(); // push existing id order
            String userid = firebaseAuth.getCurrentUser().getUid();   //to get current user
            //String eventId = firebaseAuth.getCurrentUser().getUid();//currentevent
            String firstname = mFirstName.getText().toString().trim();
            String lastname = mLastName.getText().toString().trim();
            String phone = mPhone.getText().toString().trim();
            String address = mAddress.getText().toString().trim();

            String orderStatus = "To Pay";
            String orderDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            //String paymentReceipt =
            //String itemId = m.getText().toString().trim();
            String imgPayment = "https://butlerlandscapes.com/wp-content/uploads/2013/11/pay-your-invoice.jpg";


            String newTotalPrice = String.valueOf(totalPrice);
            Log.i("names", orderId);
            PurchaseOrder purchaseOrder = new PurchaseOrder(userid,orderId,firstname,lastname,phone,address,listCart,orderStatus, orderDate, newTotalPrice ,imgPayment);

            //volRef.child(id).setValue(specificEvent);
            purchaseOrderRef.child(userid).child(orderId).setValue(purchaseOrder);
            Toast.makeText(this, "Order has been Placed", Toast.LENGTH_LONG).show();
            //nak paggil kat page lain
            Intent myIntent = new Intent(ShoppingCart.this, PaymentReceipt.class);
            myIntent.putExtra("orderId", orderId);
            myIntent.putExtra("firstname", String.valueOf(mFirstName));
            myIntent.putExtra("lastname", String.valueOf(mLastName));
            myIntent.putExtra("phone", String.valueOf(mPhone));
            myIntent.putExtra("userid", userid);
            //myIntent.putExtra("eventname", String.valueOf(mNewsName));

            startActivity(myIntent);

            //DELETE ALL ITEM IN CART

            DatabaseReference dbCart = FirebaseDatabase.getInstance().getReference("Cart");
            dbCart.child(userid).removeValue();
        }
    }
}