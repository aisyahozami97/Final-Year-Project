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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainNews extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView mBack;
    NewsAdapter adapter;
    List<NewsAdmin> newsList;
    RecyclerView recyclerView;
    //AddOnn
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);

        //RecylerView
        recyclerView = findViewById(R.id.recyclerViewNews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainNews.this));

        newsList = new ArrayList<>();
        adapter = new NewsAdapter(MainNews.this, newsList);
        recyclerView.setAdapter(adapter);

        //DatabaseReference dbNews = FirebaseDatabase.getInstance().getReference("NewsAdmin");
        ref = FirebaseDatabase.getInstance().getReference("NewsAdmin").child("");


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_place_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Preloved Event");

        //Widget Add Button
        final FloatingActionButton addNews = (FloatingActionButton) findViewById(R.id.addNews);
        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainNews.this, AddNews.class));
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && addNews.isShown())
                    addNews.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    addNews.show();
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
                         newsList = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            newsList.add(ds.getValue(NewsAdmin.class));
                        }
                    }
                    adapter = new NewsAdapter(newsList);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainNews.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(MainNews.this, adminHomePage.class));
        }
        return super.onOptionsItemSelected(item);
    }



}
