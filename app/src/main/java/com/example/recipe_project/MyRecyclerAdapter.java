package com.example.recipe_project;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

    private ArrayList<FindItem> mFindList;
    public interface OnItemClickListener {
        void onItemClick(FindItem item);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(mFindList.get(position));
    }

    public void setFindList(List<FindItem> list){

        this.mFindList =new ArrayList<>(list);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFindList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (ImageView) itemView.findViewById(R.id.item_img);
            name = (TextView) itemView.findViewById(R.id.item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(mFindList.get(position));
                    }
                }
            });

        }

        void onBind(FindItem item){
            //profile.setImageResource(item.getRecipe_ID());
            name.setText(item.getRecipe_Name());

        }


    }
}