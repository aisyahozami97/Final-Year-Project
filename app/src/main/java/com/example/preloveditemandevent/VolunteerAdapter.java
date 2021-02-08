package com.example.preloveditemandevent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.MyViewHolder>{

    private Context context;
    BottomNavigationView bottomNavigationView;

    List<String> key;

    ArrayList<SpecificEvent> listUser;
    public VolunteerAdapter(ArrayList<SpecificEvent> listUser){
        this.listUser = listUser;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder_listvolunteer, viewGroup, false);

        context = viewGroup.getContext();

        return new VolunteerAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.firstName.setText(listUser.get(i).getVolFirstName());
        myViewHolder.lastName.setText(listUser.get(i).getVolLastName());
        myViewHolder.phone.setText(listUser.get(i).getVolPhone());
        myViewHolder.eventName.setText(listUser.get(i).getVolEventName());

        //
//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String key = list.get(i).getArtistId();
//
//
//
//                Intent intent = new Intent(context,DetailsHistoryAdminActivity.class);
//                intent.putExtra("key",key);
////                intent.putExtra("price",price);
////                intent.putExtra("location",player);
////                intent.putExtra("country",price);
////                intent.putExtra("location",player);
////                intent.putExtra("country",price);
//                context.startActivity(intent);
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView firstName,lastName, phone, eventName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.volEventName);
            firstName = itemView.findViewById(R.id.volFirstName);
            lastName = itemView.findViewById(R.id.volLastName);
            phone = itemView.findViewById(R.id.volPhone);

        }

    }

}
