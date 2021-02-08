package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PurchaseOrderDetails extends AppCompatActivity {

    DatabaseReference purchaseOrderRef;
    ArrayList<PurchaseOrder> listPurchaseOrder;
    RecyclerView recyclerViewUser;
    PurchaseOrderDetailsAdapter purchaseOrderDetailsAdapter;
    SearchView searchViewUser;
    private Toolbar mToolbar;
    //ArrayList<PurchaseOrder> listUser;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth, firebaseAuth;
    private String userID;
    private Object PurchaseOrderDetailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_order_details);

        //Firebase connection
        mAuth = FirebaseAuth.getInstance();


        //purchaseOrderRef = FirebaseDatabase.getInstance().getReference("PurchaseOrder").child(userId).child("");
        purchaseOrderRef = FirebaseDatabase.getInstance().getReference("PurchaseOrder").child("");

        recyclerViewUser = findViewById(R.id.rvPurchaseOrderDetails);
        recyclerViewUser.setHasFixedSize(true);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(PurchaseOrderDetails.this));

        //RO RV ATTACH
        listPurchaseOrder = new ArrayList<>();
        purchaseOrderDetailsAdapter = new PurchaseOrderDetailsAdapter(listPurchaseOrder);
        recyclerViewUser.setAdapter(purchaseOrderDetailsAdapter);





        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.poDetails_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Purchase Order");

        //FirebaseUser user = mAuth.getCurrentUser();
        //userID = user.getUid();
    }


    @Override
    protected void onStart() {
        super.onStart();
        String userId = mAuth.getCurrentUser().getUid();
        if(purchaseOrderRef != null){
            purchaseOrderRef.child(userId).addValueEventListener(new ValueEventListener() {
                //private Object ArrayList;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //to get current user
                    //firebaseAuth = FirebaseAuth.getInstance();

                    if(dataSnapshot.exists())
                    {

                        listPurchaseOrder = new ArrayList<>();

                        //RETRIEVE FROM MAIN ROOT TO ANOTHER CHILDREN
                        for(DataSnapshot ds2 : dataSnapshot.getChildren())
                        {
                            //User data = dataSnapshot.child(userID).getValue(User.class);
                            for(DataSnapshot ds: ds2.getChildren()) {
                                PurchaseOrder message = ds.getValue(PurchaseOrder.class);
                                PurchaseOrder messageSet = new PurchaseOrder();
                                String userid = mAuth.getCurrentUser().getUid();
                                //String userid = message.getUserId(); //ALL USER PURCHASEORDER
                                String orderId = message.getOrderId();
                                String custFirstName = message.getCustFirstName();
                                String custLastName = message.getCustLastName();
                                String custPhone = message.getCustPhone();
                                String custAddress = message.getCustAddress();

                                //String listCart = String.valueOf(message.getListCart());

                                String orderStatus = message.getOrderStatus();
                                String orderDate = message.getOrderDate();
                                String totalPrice = message.getTotalPrice();
                                String paymentReceipt = message.getPaymentReceipt();

                                PurchaseOrder fire = new PurchaseOrder( userid,  orderId,  custFirstName,  custLastName,  custPhone,  custAddress, orderStatus,  orderDate,  totalPrice,  paymentReceipt);

                                listPurchaseOrder.add(fire);
                            }
                        }




                    }
                    //PurchaseOrderDetailsAdapter purchaseorder = new PurchaseOrderDetailsAdapter(listPurchaseOrder);
                    // recyclerViewUser.setAdapter(purchaseorder);
                    PurchaseOrderDetailsAdapter purchaseOrderDetailsAdapter = new PurchaseOrderDetailsAdapter(listPurchaseOrder);
                    recyclerViewUser.setAdapter(purchaseOrderDetailsAdapter);
                    //purchaseOrderDetailsAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PurchaseOrderDetails.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }



    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(PurchaseOrderDetails.this, adminHomePage.class));
        }
        return super.onOptionsItemSelected(item);
    }


}