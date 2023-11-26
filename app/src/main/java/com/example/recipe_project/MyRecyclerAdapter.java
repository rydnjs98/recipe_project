package com.example.recipe_project;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
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

            HashMap<Integer, Integer> images = new HashMap<Integer, Integer>();

            images.put( 1, Integer.valueOf( R.drawable.recipe_1 ) );
            images.put( 2, Integer.valueOf( R.drawable.recipe_2 ) );
            images.put( 3, Integer.valueOf( R.drawable.recipe_3 ) );
            images.put( 4, Integer.valueOf( R.drawable.recipe_4) );
            images.put( 5, Integer.valueOf( R.drawable.recipe_5 ) );
            images.put( 6, Integer.valueOf( R.drawable.recipe_6 ) );
            images.put( 7, Integer.valueOf( R.drawable.recipe_7 ) );
            images.put( 8, Integer.valueOf( R.drawable.recipe_8 ) );
            images.put( 9, Integer.valueOf( R.drawable.recipe_9) );
            images.put( 10, Integer.valueOf( R.drawable.recipe_10 ) );
            images.put( 11, Integer.valueOf( R.drawable.recipe_11 ) );
            images.put( 12, Integer.valueOf( R.drawable.recipe_12 ) );
            images.put( 13, Integer.valueOf( R.drawable.recipe_13 ) );
            images.put( 14, Integer.valueOf( R.drawable.recipe_14 ) );
            images.put( 15, Integer.valueOf( R.drawable.recipe_15 ) );
            images.put( 16, Integer.valueOf( R.drawable.recipe_16 ) );
            images.put( 17, Integer.valueOf( R.drawable.recipe_17 ) );
            images.put( 18, Integer.valueOf( R.drawable.recipe_18 ) );
            images.put( 19, Integer.valueOf( R.drawable.recipe_19 ) );
            images.put( 20, Integer.valueOf( R.drawable.recipe_20 ) );







            profile.setImageResource(images.get(item.getRecipe_ID()).intValue() );
            name.setText(item.getRecipe_Name());


        }


    }
}