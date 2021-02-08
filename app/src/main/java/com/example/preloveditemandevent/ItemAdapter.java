package com.example.preloveditemandevent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>  {
    Context mCntxItem;
    private List<ItemAdmin> itemList;

    public ItemAdapter(Context mCntxItem, List<ItemAdmin> itemList) {
        this.mCntxItem = mCntxItem;
        this.itemList = itemList;
    }

    public ItemAdapter(List<ItemAdmin> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        mCntxItem = parent.getContext();
        //NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int i) {

        final ItemAdmin item = itemList.get(i);

        holder.itemName.setText(item.getItemName());
        holder.itemPrice.setText(item.getItemPrice());
        holder.itemCategory.setText(item.getItemCategory());
        holder.itemAvailability.setText(item.getItemAvailability());

        Picasso.get().load(item.getItemImage()).into(holder.imageItem);//using picasso to load image

        holder.item_cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //String news_desc = newsList.get(i).getNewsDesc();
                //ADA KAITAN UNTUK RETRIVE KAT UPDATEITEM CLASS
                Intent intent = new Intent(mCntxItem, UpdateItem.class);
                intent.putExtra("itemName", String.valueOf(item.getItemName()));
                intent.putExtra("itemDesc", String.valueOf(item.getItemDesc()));
                intent.putExtra("itemPrice", String.valueOf(item.getItemPrice()));
                intent.putExtra("itemCategory", String.valueOf(item.getItemCategory()));
                intent.putExtra("itemAvailability", String.valueOf(item.getItemAvailability()));
                intent.putExtra("itemDate", String.valueOf(item.getItemDate()));
                intent.putExtra("itemId", String.valueOf(item.getItemId()));
                intent.putExtra("imgurl", String.valueOf(item.getItemImage()));
                mCntxItem.startActivity(intent);

            }

        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String itemName = item.getItemName();
                String itemPrice = item.getItemPrice();
                String itemCategory = item.getItemCategory();
                String itemAvailability = item.getItemAvailability();
                String itemId = item.getItemId();

                DatabaseReference dbItem = FirebaseDatabase.getInstance().getReference("ItemAdmin");
                dbItem.child(itemId).removeValue();

                //set url of image to storageref
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(item.getItemImage());
                // Delete the file
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });

                //Toolbar
                // Remove the item on remove/button click
                itemList.remove(i);
                /*
                    Parameters
                        i : Position of the item that has now been removed
                */
                notifyItemRemoved(i);

                /*
                    Parameters
                        positionStart : Position of the first item that has changed
                        itemCount : Number of items that have changed
                */
                notifyItemRangeChanged(i,itemList.size());

                // Show the removed item label
                Toast.makeText(mCntxItem, itemName + " has been removed",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemCategory, itemAvailability;
        LinearLayout parentLayout;
        CardView item_cv;
        ImageView btnDelete, imageItem;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemCategory = itemView.findViewById(R.id.tvItemCategory);
            itemAvailability = itemView.findViewById(R.id.tvItemAvailability);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            item_cv = itemView.findViewById(R.id.item_cv);
            imageItem = itemView.findViewById(R.id.imageItem);
            btnDelete = itemView.findViewById(R.id.deleteItem);

        }
    }

}

