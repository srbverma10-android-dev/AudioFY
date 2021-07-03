package com.sourabhverma.audiofy.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourabhverma.audiofy.Notifications.CreateNotification;
import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.helperClasses.BottomSheetHelperClass;
import com.sourabhverma.audiofy.models.upperSlideImages;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class upperSlideAdapter extends RecyclerView.Adapter<upperSlideAdapter.uppersliderViewHolder> {


    public upperSlideAdapter(List<upperSlideImages> upperSlideImagesList, Activity activity) {
        this.upperSlideImagesList = upperSlideImagesList;
        this.activity = activity;
    }

    private final List<upperSlideImages> upperSlideImagesList;
    private final Activity activity;

    @NonNull
    @Override
    public upperSlideAdapter.uppersliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new uppersliderViewHolder(

                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.upperslide_item, parent, false
                )

        );
    }

    @Override
    public void onBindViewHolder(@NonNull upperSlideAdapter.uppersliderViewHolder holder, int position) {

            holder.setImageView(upperSlideImagesList.get(position));

            holder.setOnClickListenerInImageView(activity,upperSlideImagesList, position);

    }

    @Override
    public int getItemCount() {
        return upperSlideImagesList.size();
    }

    public static class uppersliderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public uppersliderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewUpperSlide);


        }


        public void setImageView(upperSlideImages upperSlideImages) {

            Bitmap decodedImage;
            String encoded_Image = upperSlideImages.getLongSongPhoto();
            byte[] imageInByte = Base64.decode(encoded_Image, Base64.DEFAULT);
            decodedImage = BitmapFactory.decodeByteArray(imageInByte,0, imageInByte.length);

            imageView.setImageBitmap(decodedImage);

        }

        public void setOnClickListenerInImageView(Activity activity, List<upperSlideImages> upperSlideImagesList, int position) {

            SharedPreferences sharedPreferences = activity.getSharedPreferences("CURRENT_SONG", MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            imageView.setOnClickListener(view -> {

                BottomSheetHelperClass.getInstance(activity, upperSlideImagesList, null, position );
                UpdateUiOfBottomSheet(sharedPreferencesEditor,upperSlideImagesList,position);
                CreateNotification.createNotification(activity,upperSlideImagesList,null,position,R.drawable.skip_previous,R.drawable.pause,R.drawable.skip_next);

            });

        }

        private Bitmap changeStringToBitmap(String songPhoto) {
            Bitmap decodedImage;
            byte[] imageInByte = Base64.decode(songPhoto, Base64.DEFAULT);
            decodedImage = BitmapFactory.decodeByteArray(imageInByte,0, imageInByte.length);

            return decodedImage;
        }

        private void UpdateUiOfBottomSheet(SharedPreferences.Editor sharedPreferencesEditor, List<upperSlideImages> upperSlideImagesList, int position) {

            sharedPreferencesEditor.putString("SONG_ID", upperSlideImagesList.get(position).getSongId());
            sharedPreferencesEditor.putString("SONG_NAME", upperSlideImagesList.get(position).getSongName());
            sharedPreferencesEditor.putString("SONG_PHOTO", upperSlideImagesList.get(position).getSongPhoto());
            sharedPreferencesEditor.putInt("IS_PLAYING", 0);
            sharedPreferencesEditor.apply();

        }

    }
}
