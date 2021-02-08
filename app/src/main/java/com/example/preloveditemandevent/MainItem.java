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

public class MainItem extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView mBack;
    ItemAdapter itemAdapter;
    List<ItemAdmin> itemList;
    RecyclerView recyclerView;
    SearchView searchViewCategory;
    //AddOnn
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item);

        //RecylerView
        recyclerView = findViewById(R.id.recyclerViewItem);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainItem.this));
        searchViewCategory = findViewById(R.id.searchViewItemCategory);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(MainItem.this, itemList);
       // searchViewCategory= findViewById(R.id.searchViewItemCategory);
        recyclerView.setAdapter(itemAdapter);

        //DatabaseReference dbNews = FirebaseDatabase.getInstance().getReference("NewsAdmin");
        ref = FirebaseDatabase.getInstance().getReference("ItemAdmin").child("");


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("ALL ITEM");

        //Widget Add Button
        final FloatingActionButton addItem = (FloatingActionButton) findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainItem.this, AddItem.class));
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && addItem.isShown())
                    addItem.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    addItem.show();
                }
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
                        itemList = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            itemList.add(ds.getValue(ItemAdmin.class));
                        }
                    }
                    itemAdapter = new ItemAdapter(itemList);
                    recyclerView.setAdapter(itemAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainItem.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
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
        ItemAdapter itemAdapter = new ItemAdapter(myList);
        recyclerView.setAdapter(itemAdapter);
    }

    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(MainItem.this, adminHomePage.class));
        }
        return super.onOptionsItemSelected(item);
    }



}
