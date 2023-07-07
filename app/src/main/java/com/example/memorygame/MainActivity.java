package com.example.memorygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, ImageDownloader.OnImageUrlsReadyListener {

    private GridView gridView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls;
    private ImageDownloader imageDownloader;

    private String websiteUrl;
    private EditText etSearch;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        websiteUrl = "https://stocksnap.io"; // Replace with the desired website URL
        imageUrls = new ArrayList<>();
        gridView = findViewById(R.id.downloadGridView);
        etSearch = findViewById(R.id.et_url);
        imageDownloader = new ImageDownloader();
        imageDownloader.setOnImageUrlsReadyListener(this);
//        imageDownloader.setOnImageUrlsReadyListener(new ImageDownloader.OnImageUrlsReadyListener() {
//            @Override
//            public void onImageUrlsReady(List<String> fetchUrls) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imageUrls.clear();
//                        imageUrls.addAll(fetchUrls);
//                        imageAdapter = new ImageAdapter(MainActivity.this, MainActivity.this.imageUrls);
//                        gridView.setAdapter(imageAdapter);
//                        imageAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
        etSearch.setOnTouchListener(this);
        imageDownloader.fetchImagesFromWebsite(websiteUrl,20);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int iconRight = etSearch.getRight() - etSearch.getCompoundDrawables()[2].getBounds().width();
            if (event.getRawX() >= iconRight) {
                websiteUrl = etSearch.getText().toString();
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                imageDownloader.setOnImageUrlsReadyListener(this);
                imageDownloader.fetchImagesFromWebsite(websiteUrl,20);
                return true;  // Consume the touch event
            }
        }
        return false;
    }

    @Override
    public void onImageUrlsReady(List<String> fetchUrls) {
        runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageUrls.clear();
                        imageUrls.addAll(fetchUrls);
                        imageAdapter = new ImageAdapter(MainActivity.this, imageUrls);
                        gridView.setAdapter(imageAdapter);
                        imageAdapter.notifyDataSetChanged();
                    }
                });
    }
}