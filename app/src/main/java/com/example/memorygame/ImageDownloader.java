package com.example.memorygame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ImageDownloader {

    private static final String TAG = "ImageDownloader";
//    private WeakReference<Activity> activityRef;
    private List<String> imageUrls;
    private int imageCount;
    private int downloadedCount;
    private OnImageUrlsReadyListener onImageUrlsReadyListener;

    public ImageDownloader() {
//        this.activityRef = new WeakReference<>(activity);
        this.imageUrls = new ArrayList<>();
        this.imageCount = 0;
        this.downloadedCount = 0;
    }

    public void fetchImagesFromWebsite(String websiteUrl, int limit) {
        new Thread(() -> {
            try {
                downloadedCount =0;
                imageUrls.clear();
                // Fetch the website content using Jsoup
                Document document = Jsoup.connect(websiteUrl).get();
                // Find all image elements in the HTML
                Elements imageElements = document.select("img");

                // Extract the image URLs up to the specified limit
                int count = 0;
                for (Element element : imageElements) {
                    if (count >= limit) {
                        break;
                    }

                    String imageUrl = element.absUrl("src");
                    if (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png") || imageUrl.endsWith(".jpeg")) {
                        imageUrls.add(imageUrl);
                        count++;
                    }
                }

                imageCount = imageUrls.size();

                // Download and store the images using Picasso
                for (String imageUrl : imageUrls) {
                    Picasso.get().load(imageUrl);
                    downloadedCount++;
                }

                if (downloadedCount == imageCount) {
                    if (onImageUrlsReadyListener != null) {
                        onImageUrlsReadyListener.onImageUrlsReady(imageUrls);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface OnImageUrlsReadyListener {
        void onImageUrlsReady(List<String> imageUrls);
    }

    public void setOnImageUrlsReadyListener(OnImageUrlsReadyListener listener) {
        this.onImageUrlsReadyListener = listener;
    }

//
//    private void runOnUiThread(Runnable runnable) {
//        Activity activity = activityRef.get();
//        if (activity != null) {
//            activity.runOnUiThread(runnable);
//        }
//    }
//    private class ImageTarget implements Target {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//
//        }
//
//
//        @Override
//        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//            // Handle failure cases if any
//            runOnUiThread(() -> {
//                Log.d(TAG, "Image download failed: " + e.getMessage());
//            });
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//            // Pre-loading preparation if required
//        }
//    }

}







