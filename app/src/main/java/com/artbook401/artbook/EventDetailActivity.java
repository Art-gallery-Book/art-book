package com.artbook401.artbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = "EventDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#f46b45"));

        // Set BackgroundDrawable
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        ImageView eventDetailImage = (ImageView) findViewById(R.id.eventDetailImage);

        eventDetailImage.setBackgroundResource(R.drawable.event_image);

        Intent intent = getIntent();

        ((TextView) findViewById(R.id.eventDetailDate)).setText(intent.getStringExtra("eventDate"));
        ((TextView) findViewById(R.id.eventDetailName)).setText(intent.getStringExtra("eventName"));
        ((TextView) findViewById(R.id.eventDetailDesc)).setText(intent.getStringExtra("eventDescription"));

        //        Uri.parse("geo:" + intent.getStringExtra("eventLat") + "," + intent.getStringExtra("eventLon")));

        String location = "geo:" + intent.getDoubleExtra("eventLat", 37.7749) + "," + intent.getDoubleExtra("eventLon", -122.4194);

        Log.i(TAG, "onCreate: " + location);
        findViewById(R.id.eventDetailLocationBTN).setOnClickListener(view ->{
            Intent openMap = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(location));
            startActivity(openMap);
        });
    }
}