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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>  {
    Context mCntx;
    private List<NewsAdmin> newsList;

    public NewsAdapter(Context mCntx, List<NewsAdmin> newsList) {
        this.mCntx = mCntx;
        this.newsList = newsList;
    }

    public NewsAdapter(List<NewsAdmin> newsList) {
        this.newsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
        mCntx = parent.getContext();
        //NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int i) {
        //refer to NewsAdmin
        final NewsAdmin news = newsList.get(i);
        holder.newsName.setText(news.getNewsName());
        holder.news_desc.setText(news.getNewsDesc());
        holder.newsLocation.setText(news.getNewsLocation());
        holder.newsDate.setText(news.getNewsDate());
        holder.newsTime.setText(news.getNewsTime());
        holder.numVolunteer.setText(news.getNumVolunteer());
        //holder.news_desc.setText(news.getNewsDesc());

        Picasso.get().load(news.getNewsImage()).into(holder.news_pic);//using picasso to load image

        holder.news_cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //String news_desc = newsList.get(i).getNewsDesc();

                Intent intent = new Intent(mCntx, UpdateNews.class);
                intent.putExtra("newsId", String.valueOf(news.getNewsId()));
                intent.putExtra("imgurl", String.valueOf(news.getNewsImage()));
                intent.putExtra("newsName", String.valueOf(news.getNewsName()));
                intent.putExtra("news_desc", String.valueOf(news.getNewsDesc()));
                intent.putExtra("newsLocation", String.valueOf(news.getNewsLocation()));
                intent.putExtra("newsDate", String.valueOf(news.getNewsDate()));
                intent.putExtra("newsTime", String.valueOf(news.getNewsTime()));
                intent.putExtra("numVolunteer", String.valueOf(news.getNumVolunteer()));

                mCntx.startActivity(intent);

            }

        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                String newsId = news.getNewsId();
                String newsName = news.getNewsName();
                String newsDesc = news.getNewsDesc();
                String newsLocation = news.getNewsLocation();
                String newsDate = news.getNewsDate();
                String newsTime = news.getNewsTime();
                String numVolunteer = news.getNumVolunteer();

                DatabaseReference dbNews = FirebaseDatabase.getInstance().getReference("NewsAdmin");
                dbNews.child(newsId).removeValue();

                //set url of image to storageref
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(news.getNewsImage());
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
                newsList.remove(i);
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
                notifyItemRangeChanged(i,newsList.size());

                // Show the removed item label
                Toast.makeText(mCntx, newsDesc + " has been removed",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView news_desc, newsName, newsLocation, newsDate,newsTime, numVolunteer;
        LinearLayout parentLayout;
        CardView news_cv;
        ImageView btnDelete, news_pic;
        //Spinner newsTme, numVolunteer;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsName = itemView.findViewById(R.id.newsName);
            news_desc = itemView.findViewById(R.id.newsDesc);
            newsLocation = itemView.findViewById(R.id.newsLocation);
            newsDate = itemView.findViewById(R.id.newsDate);
            newsTime = itemView.findViewById(R.id.spinnerNewsTime);
            numVolunteer = itemView.findViewById(R.id.numVolunteer);

            parentLayout = itemView.findViewById(R.id.parent_layout);
            news_cv = itemView.findViewById(R.id.news_cv);
            news_pic = itemView.findViewById(R.id.image);
            btnDelete = itemView.findViewById(R.id.delete);

        }
    }

}

