package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListPurchaseOrder extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView mBack;
    ListPurchaseOrderAdapter listPurchaseOrderAdapter;
    ArrayList<PurchaseOrder> purchaseOrderList;
    RecyclerView recyclerView;
    //SearchView searchViewCategory;
    //AddOnn
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_order_details);

        //RecylerView
        recyclerView = findViewById(R.id.recyclerViewPurchaseOrder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListPurchaseOrder.this));


        purchaseOrderList = new ArrayList<>();
        listPurchaseOrderAdapter = new ListPurchaseOrderAdapter(ListPurchaseOrder.this, purchaseOrderList);
        // searchViewCategory= findViewById(R.id.searchViewItemCategory);
        recyclerView.setAdapter(listPurchaseOrderAdapter);

        //DatabaseReference dbNews = FirebaseDatabase.getInstance().getReference("NewsAdmin");
        ref = FirebaseDatabase.getInstance().getReference("PurchaseOrder").child("");


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("PURCHASE ORDER");



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        purchaseOrderList = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            for(DataSnapshot ds2: ds.getChildren()) {
                                purchaseOrderList.add(ds2.getValue(PurchaseOrder.class));
                            }
                        }
                    }
                    listPurchaseOrderAdapter = new ListPurchaseOrderAdapter(purchaseOrderList);
                    recyclerView.setAdapter(listPurchaseOrderAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListPurchaseOrder.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }



    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(ListPurchaseOrder.this, adminHomePage.class));
        }
        return super.onOptionsItemSelected(item);
    }



}
