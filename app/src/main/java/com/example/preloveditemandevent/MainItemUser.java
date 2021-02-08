package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MainItemUser extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView mBack;
    private ImageView mLogout, mBtnShoppingCart;
    ItemAdapterUser itemAdapterUser;
    List<ItemAdmin> itemList;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    //AddOnn
    DatabaseReference ref;
    SearchView searchViewCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item_user);

        //RecylerView
        recyclerView = findViewById(R.id.recyclerViewItem);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainItemUser.this));

        itemList = new ArrayList<>();
        itemAdapterUser = new ItemAdapterUser(MainItemUser.this, itemList);
        recyclerView.setAdapter(itemAdapterUser);
        searchViewCategory = findViewById(R.id.searchViewItemCategoryUser);

        //DatabaseReference dbNews = FirebaseDatabase.getInstance().getReference("NewsAdmin");
        ref = FirebaseDatabase.getInstance().getReference("ItemAdmin").child("");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("ITEM");

        //Logout
        mLogout = (ImageView) findViewById(R.id.btn_signOut);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainItemUser.this,"Successfully Logout",Toast.LENGTH_SHORT).show();
                logout();
            }
        });

        //ShoppingCart
        mBtnShoppingCart = (ImageView) findViewById(R.id.btn_shoppingCart);
        mBtnShoppingCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Toast.makeText(MainItemUser.this,"Directly to Shopping Cart",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), ShoppingCart.class).putExtra("Mode", 1));
        }});

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
                        itemList = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            itemList.add(ds.getValue(ItemAdmin.class));
                        }
                    }
                    itemAdapterUser = new ItemAdapterUser(itemList);
                    recyclerView.setAdapter(itemAdapterUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainItemUser.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        if(searchViewCategory != null){
            searchViewCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        ArrayList<ItemAdmin> myList = new ArrayList<>();
        for (ItemAdmin object : itemList)
        {
            if(object.itemCategory.toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        ItemAdapterUser itemAdapterUser = new ItemAdapterUser(myList);
        recyclerView.setAdapter(itemAdapterUser);
    }


    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(MainItemUser.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //Logout Method
    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainItemUser.this, Login_Form.class));
        finish();
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainItemUser.this, Login_Form.class));
                    finish();
                }
            }
        };
    }

}
