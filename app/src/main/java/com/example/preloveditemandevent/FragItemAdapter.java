package com.example.preloveditemandevent;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FragItemAdapter extends RecyclerView.Adapter<FragItemAdapter.ItemViewHolder>  {

    Context mCntxItem;
    private List<ItemAdmin> itemList;


    public FragItemAdapter(Context mCntxItem, List<ItemAdmin> itemList) {
        this.mCntxItem = mCntxItem;
        this.itemList = itemList;
    }

    @Override
    public FragItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCntxItem).inflate(R.layout.frag_item_layout, parent, false);
        FragItemAdapter.ItemViewHolder itemViewHolder = new FragItemAdapter.ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(FragItemAdapter.ItemViewHolder holder, final int position) {

        final ItemAdmin item = itemList.get(position);

        holder.itemName.setText(item.getItemName());
        Picasso.get().load(item.getItemImage()).into(holder.imageItem);//using picasso to load image
    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        LinearLayout parentLayout;
        CardView item_cv;
        ImageView imageItem;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            item_cv = itemView.findViewById(R.id.item_cv);
            imageItem = itemView.findViewById(R.id.imageItem);


        }
    }
}

