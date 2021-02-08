package com.example.preloveditemandevent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class ListVolunteerActivity extends AppCompatActivity {
    private TextView mEventName;
    DatabaseReference eventRef;
    ArrayList<SpecificEvent> listUser;
    RecyclerView recyclerViewUser;
    SearchView searchViewUser;
    VolunteerAdapter volunteerAdapter;
    private Toolbar mToolbar;
    private static final String TAG = "ListVolunteerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_volunteer);

        eventRef = FirebaseDatabase.getInstance().getReference("SpecificEvent").child("");


        recyclerViewUser = findViewById(R.id.rvVolunteer);
        searchViewUser = findViewById(R.id.searchViewVolunteer);
        recyclerViewUser.setAdapter(volunteerAdapter); // MUST LETAK if nak search function

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.listvolunteer_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("List Volunteer");


    }

        @Override
        protected void onStart() {
            super.onStart();
            if(eventRef != null){
                eventRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            listUser = new ArrayList<>();
                            //RETRIEVE FROM MAIN ROOT TO ANOTHER CHILDREN
                            for(DataSnapshot ds2 : dataSnapshot.getChildren())
                            {
                                 for(DataSnapshot ds: ds2.getChildren()) {
                                    SpecificEvent message = ds.getValue(SpecificEvent.class);
                                    SpecificEvent messageSet = new SpecificEvent();
                                    String userId = message.getUserId();
                                    String volId = message.getVolId();
                                    String eventId = message.getVolEventId();
                                    String vEventName = message.getVolEventName();
                                    String vFirstName = message.getVolFirstName();
                                    String vLastName = message.getVolLastName();
                                    String vPhone = message.getVolPhone();
                                    SpecificEvent fire = new SpecificEvent(userId, volId, eventId, vFirstName, vLastName, vPhone, vEventName);

                                    listUser.add(fire);
                                }
                            }




                        }
                        VolunteerAdapter volunteerAdapter = new VolunteerAdapter(listUser);
                        recyclerViewUser.setAdapter(volunteerAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ListVolunteerActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
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

        ArrayList<SpecificEvent> myList = new ArrayList<>();
        for (SpecificEvent object : listUser)
        {
            if(object.volEventName.toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        VolunteerAdapter volunteerAdapter = new VolunteerAdapter(myList);
        recyclerViewUser.setAdapter(volunteerAdapter);
    }

    //toolBar Back to Admin Homepage
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(ListVolunteerActivity.this, adminHomePage.class));
        }
        return super.onOptionsItemSelected(item);
    }


}