package com.example.preloveditemandevent;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class News extends Fragment {


    private View NewsView;
    private RecyclerView rvNews;
    FragNewsAdapter adapter;
    List<NewsAdmin> newsList;

    private DatabaseReference mRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NewsView = inflater.inflate(R.layout.fragment_news, container, false);

        rvNews = (RecyclerView) NewsView.findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));

       /* rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FragNewsAdapter(newsList);
        rvNews.setAdapter(adapter);*/

        newsList = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("NewsAdmin");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){

                        NewsAdmin news = productSnapshot.getValue(NewsAdmin.class);
                        newsList.add(news);
                    }
                    adapter = new FragNewsAdapter(getContext(),newsList);
                    rvNews.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return NewsView;
    }



}
