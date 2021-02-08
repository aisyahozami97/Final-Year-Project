package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class ListUserActivity extends AppCompatActivity {

    DatabaseReference ref;
    ArrayList<User> listUser;
    RecyclerView recyclerViewUser;
    SearchView searchViewUser;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        ref = FirebaseDatabase.getInstance().getReference("User").child("");

        recyclerViewUser = findViewById(R.id.rvUser);
        searchViewUser = findViewById(R.id.searchViewUser);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.listuser_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("LIST USER");

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
                        listUser = new ArrayList<>();

                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            listUser.add(ds.getValue(User.class ));
                        }



                    }
                    UserAdapter userAdapter = new UserAdapter(listUser);
                    recyclerViewUser.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListUserActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
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

        ArrayList<User> myList = new ArrayList<>();
        for (User object : listUser)
        {
            if(object.userName.toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        UserAdapter userAdapter = new UserAdapter(myList);
        recyclerViewUser.setAdapter(userAdapter);
    }

    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(ListUserActivity.this, adminHomePage.class));
        }
        return super.onOptionsItemSelected(item);
    }


}