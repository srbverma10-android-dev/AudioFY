package com.sourabhverma.audiofy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.helperClasses.CacheHelperClass;
import com.sourabhverma.audiofy.models.article;
import com.sourabhverma.audiofy.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.LogRecord;

import static com.sourabhverma.audiofy.utils.Constants.TAG;

public class NewsFragmentAdapter extends RecyclerView.Adapter<NewsFragmentAdapter.NewsFragmentAdapterViewHolder> {

    public NewsFragmentAdapter(ArrayList<article> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    private ArrayList<article> arrayList;
    private Context context;

    @NonNull
    @Override
    public NewsFragmentAdapter.NewsFragmentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsFragmentAdapterViewHolder(

                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.news_fragment_item, parent, false
                )

        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFragmentAdapter.NewsFragmentAdapterViewHolder holder, int position) {

        holder.contentTextView.setText(arrayList.get(position).getDescription());
        holder.titleTextView.setText(arrayList.get(position).getTitle());
        holder.titleTextView.setText(arrayList.get(position).getTitle());
        holder.publishedAt.setText(arrayList.get(position).getPublishedAt().substring(0,10));

        try {
            Bitmap bitmap = CacheHelperClass.getImage(context,position);
            if (bitmap == null) {
                holder.downloadAndShowImages(arrayList.get(position).getUrlToImage(), position, context);
            } else  {
                holder.imageView.setImageBitmap(bitmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class NewsFragmentAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final TextView contentTextView;
        private final TextView publishedAt;
        private final ImageView imageView;

        private final Executor executor = Executors.newSingleThreadExecutor();

        private final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                byte[] bytes = msg.getData().getByteArray("ImageForNewsFragmentItem");
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageView.setImageBitmap(bmp);

            }
        };

        public NewsFragmentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleInNewsFragmentItem);
            contentTextView = itemView.findViewById(R.id.contentInNewsFragmentItem);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            imageView = itemView.findViewById(R.id.imageViewInNewsFragmentItem);

        }


        public void downloadAndShowImages(String url, int position, Context context) {

            Log.d(TAG, "downloadAndShowImages: " + position);
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    InputStream inputStream;
                    Bitmap bitmap = null;
                    try {
                        URL imageUrl = new URL(url);
                        inputStream = (InputStream) imageUrl.getContent();
                        bitmap = BitmapFactory.decodeStream(inputStream);

                        CacheHelperClass.putImage(context,position,bitmap);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putByteArray("ImageForNewsFragmentItem", byteArray);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }



            });


        }


    }
}
