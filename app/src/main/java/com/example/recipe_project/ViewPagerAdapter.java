package com.example.recipe_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder> {
    private ArrayList<Integer> item;

    public ViewPagerAdapter(ArrayList<Integer> idolList) {
        item = idolList;
    }

    @Override
    public PagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img, parent, false);
        return new PagerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onBindViewHolder(PagerViewHolder holder, int position) {
        holder.idol.setImageResource(item.get(position));
    }

    public class PagerViewHolder extends RecyclerView.ViewHolder {
        public ImageView idol;

        public PagerViewHolder(View itemView) {
            super(itemView);
            idol = itemView.findViewById(R.id.imageView_idol);
        }
    }
}
