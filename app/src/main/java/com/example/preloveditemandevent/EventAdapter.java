package com.example.preloveditemandevent;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;

    List<String> key;
    ArrayList<Event> eventList;

    public EventAdapter(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eventadapter_list, viewGroup, false);

        context = viewGroup.getContext();

        return new EventAdapter.MyViewHolder(view);
    }

    public EventAdapter(Context c) {
        this.context = c;

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.event_name.setText(eventList.get(i).getEvent_name());
        Picasso.get().load(eventList.get(i).getEvent_image()).into(myViewHolder.image);



        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bName = eventList.get(i).getEvent_name();
                String pic = eventList.get(i).getEvent_image();

                Log.i("loz", bName);

                Intent intent = new Intent(context, UpdateEvent.class);
                intent.putExtra("brand_name", bName);
                intent.putExtra("imgurl", pic);
                context.startActivity(intent);
            }
        });


        myViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name = eventList.get(i).getEvent_name();
                String event_id = eventList.get(i).getEvent_id();

                DatabaseReference dbEvent = FirebaseDatabase.getInstance().getReference("Event");
                dbEvent.child(event_id).removeValue();

                //set url of image to storageref
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(eventList.get(i).getEvent_image());
                // Delete the file
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });

                //Toolbar
                // Remove the item on remove/button click
                eventList.remove(i);
                /*
                    Parameters
                        position : Position of the item that has now been removed
                */
                notifyItemRemoved(i);

                /*
                    Parameters
                        positionStart : Position of the first item that has changed
                        itemCount : Number of items that have changed
                */
                notifyItemRangeChanged(i, eventList.size());

                // Show the removed item label
                Toast.makeText(context, event_name + " has been removed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView event_name;
        ImageView image, btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            event_name = itemView.findViewById(R.id.event_name);
            image = itemView.findViewById(R.id.image);
            btnDelete = itemView.findViewById(R.id.delete);

        }
    }

}
