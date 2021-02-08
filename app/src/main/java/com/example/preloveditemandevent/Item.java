package com.example.preloveditemandevent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Item extends Fragment {


    private View itemsView;
    private RecyclerView rvItem;
    FragItemAdapter adapterItem;
    List<ItemAdmin> itemList;

    private DatabaseReference mRefItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemsView = inflater.inflate(R.layout.fragment_item, container, false);

        rvItem = (RecyclerView) itemsView.findViewById(R.id.rv_item);
        rvItem.setLayoutManager(new LinearLayoutManager(getContext()));

       /* rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FragNewsAdapter(newsList);
        rvNews.setAdapter(adapter);*/

        itemList = new ArrayList<>();
        mRefItem = FirebaseDatabase.getInstance().getReference("ItemAdmin");
        mRefItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){

                        ItemAdmin item = productSnapshot.getValue(ItemAdmin.class);
                        itemList.add(item);
                    }
                    adapterItem = new FragItemAdapter(getContext(),itemList);
                    rvItem.setAdapter(adapterItem);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return itemsView;
    }



}
