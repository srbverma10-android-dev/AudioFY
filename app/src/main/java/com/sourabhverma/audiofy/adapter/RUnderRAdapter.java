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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourabhverma.audiofy.Notifications.CreateNotification;
import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.helperClasses.BottomSheetHelperClass;
import com.sourabhverma.audiofy.models.RUnderR;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RUnderRAdapter extends RecyclerView.Adapter<RUnderRAdapter.RUnderRViewHolder> {


    public RUnderRAdapter(ArrayList<RUnderR> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    private final ArrayList<RUnderR> arrayList;
    private final Activity activity;

    @NonNull
    @Override
    public RUnderRAdapter.RUnderRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RUnderRViewHolder(

                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.r_under_r_item, parent, false
                )

        );
    }

    @Override
    public void onBindViewHolder(@NonNull RUnderRAdapter.RUnderRViewHolder holder, int position) {

        Bitmap decodedImage;
        String encoded_Image = arrayList.get(position).getSongPhoto();
        byte[] imageInByte = Base64.decode(encoded_Image, Base64.DEFAULT);
        decodedImage = BitmapFactory.decodeByteArray(imageInByte,0, imageInByte.length);

        holder.setImageView(decodedImage);

        holder.setTextView(arrayList.get(position).getSongName());

        holder.onClickListener(activity,arrayList, position);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RUnderRViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textView;

        public void onClickListener(Activity activity, ArrayList<RUnderR> arrayList, int position) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("CURRENT_SONG", MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            imageView.setOnClickListener(view -> {
                CreateNotification.createNotification(activity,null,arrayList,position,R.drawable.skip_previous,R.drawable.pause,R.drawable.skip_next);
                BottomSheetHelperClass.getInstance(activity,null,arrayList,position);
                UpdateUiOfBottomSheet(sharedPreferencesEditor,arrayList,position);
            });

        }

        public RUnderRViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewRUnderR);
            textView = itemView.findViewById(R.id.textViewInRunderR);

        }


        void setImageView(Bitmap decodedImage){
            imageView.setImageBitmap(decodedImage);
        }

        void setTextView(String text) { textView.setText(text); }

        private void UpdateUiOfBottomSheet(SharedPreferences.Editor sharedPreferencesEditor, ArrayList<RUnderR> arrayList, int position) {

            sharedPreferencesEditor.putString("SONG_ID", arrayList.get(position).getSongId());
            sharedPreferencesEditor.putString("SONG_NAME", arrayList.get(position).getSongName());
            sharedPreferencesEditor.putString("SONG_PHOTO", arrayList.get(position).getSongPhoto());
            sharedPreferencesEditor.putInt("IS_PLAYING", 0);
            sharedPreferencesEditor.apply();
        }
    }
}
