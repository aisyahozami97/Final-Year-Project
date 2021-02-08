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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    private Context context;
    BottomNavigationView bottomNavigationView;

    List<String> key;

    ArrayList<User> listUser;
    public UserAdapter(ArrayList<User> listUser){
        this.listUser = listUser;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder_listuser, viewGroup, false);

        context = viewGroup.getContext();

        return new UserAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.email.setText(listUser.get(i).getUserName());
        myViewHolder.firstName.setText(listUser.get(i).getFirstName());
        myViewHolder.lastName.setText(listUser.get(i).getLastName());
        myViewHolder.phoneNo.setText(listUser.get(i).getPhoneNo());
        myViewHolder.userName.setText(listUser.get(i).getUserName());
        myViewHolder.address.setText(listUser.get(i).getAddress());

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

        TextView email, firstName,lastName, phoneNo, userName, address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.email);
            userName = itemView.findViewById(R.id.userName);
            firstName = itemView.findViewById(R.id.firstname);
            lastName = itemView.findViewById(R.id.lastname);
            phoneNo = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.address);



        }

    }

}
