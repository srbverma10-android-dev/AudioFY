package com.sourabhverma.audiofy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.models.dataForRecyclerView;

import java.util.List;

public class otherPlaylistAdapter extends RecyclerView.Adapter<otherPlaylistAdapter.otherPlaylistViewHolder> {


    public otherPlaylistAdapter(List<dataForRecyclerView> dataForRecyclerViews, Activity activity) {
        this.dataForRecyclerViews = dataForRecyclerViews;
        this.activity = activity;
    }

    private final List<dataForRecyclerView> dataForRecyclerViews;
    private final Activity activity;

    @NonNull
    @Override
    public otherPlaylistAdapter.otherPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new otherPlaylistViewHolder(

                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.otherplaylistitem, parent, false
                )

        );
    }

    @Override
    public void onBindViewHolder(@NonNull otherPlaylistAdapter.otherPlaylistViewHolder holder, int position) {

        holder.setRecyclerView(new RUnderRAdapter(dataForRecyclerViews.get(position).getArray(),activity));
        holder.setTextView(dataForRecyclerViews.get(position));

    }

    @Override
    public int getItemCount() {
        return dataForRecyclerViews.size();
    }

    public static class otherPlaylistViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final RecyclerView recyclerView;

        public otherPlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView3);
            recyclerView = itemView.findViewById(R.id.recyclerViewUnderRecyclerView);

            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);

        }

        void setTextView(dataForRecyclerView dataForRecyclerView){
            textView.setText(dataForRecyclerView.getName());
        }

        void setRecyclerView(RUnderRAdapter rUnderRAdapter){
            recyclerView.setAdapter(rUnderRAdapter);
        }

    }
}
