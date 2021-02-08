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

public class FragNewsAdapter extends RecyclerView.Adapter<FragNewsAdapter.NewsViewHolder>  {

    Context mCntx;
    private List<NewsAdmin> newsList;


    public FragNewsAdapter(Context mCntx, List<NewsAdmin> newsList) {
        this.mCntx = mCntx;
        this.newsList = newsList;
    }

    @Override
    public FragNewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCntx).inflate(R.layout.frag_news_layout, parent, false);
        FragNewsAdapter.NewsViewHolder newsViewHolder = new FragNewsAdapter.NewsViewHolder(view);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(FragNewsAdapter.NewsViewHolder holder, final int position) {

        final NewsAdmin news = newsList.get(position);

        holder.news_desc.setText(news.getNewsDesc());
        Picasso.get().load(news.getNewsImage()).into(holder.news_pic);//using picasso to load image
    }

    @Override
    public int getItemCount()
    {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView news_desc;
        LinearLayout parentLayout;
        CardView news_cv;
        ImageView news_pic;

        public NewsViewHolder(View itemView) {
            super(itemView);

            news_desc = itemView.findViewById(R.id.newsDesc);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            news_cv = itemView.findViewById(R.id.news_cv);
            news_pic = itemView.findViewById(R.id.image);


        }
    }
}

