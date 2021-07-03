package com.sourabhverma.audiofy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.models.slideItem;

import java.util.List;

public class sliderAdapter extends RecyclerView.Adapter<sliderAdapter.sliderViewHolder>{


    public sliderAdapter(List<slideItem> sliderItems) {
        this.sliderItems = sliderItems;
    }

    private final List<slideItem> sliderItems;

    @NonNull
    @Override
    public sliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new sliderViewHolder(

                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item, parent, false
                )

        );
    }

    @Override
    public void onBindViewHolder(@NonNull sliderViewHolder holder, int position) {

        holder.setImageView(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    static class sliderViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;

        public sliderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageSlide);

        }

        void setImageView(slideItem sliderItem){

            imageView.setImageResource(sliderItem.getImage());

        }

    }

}
