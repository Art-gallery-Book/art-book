package com.artbook401.artbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.artbook401.artbook.adapters.EventsAdapter;
import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    private static final String TAG = "EventsActivity";
    private EventsAdapter eventsAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

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

        getEvents();
        Log.i(TAG, "onCreate: " + eventList);

    }

    public void getEvents() {
        eventList = new ArrayList<>();
        Amplify.API.query(ModelQuery.list(Event.class),
                success -> {
//            currentUser=success.getData();
                    Log.i(TAG, "onCreate: hi queryyyyyyyyyyyyyyyyy" + success.getData());
                    for (Event event : success.getData()) {
                        eventList.add(event);

                        Log.i(TAG, "onCreate: hi useeeeeeeeeer" + eventList);

                    }
                    eventsAdapter = new EventsAdapter(eventList, position -> {
                        Intent goToDetailsIntent = new Intent(getBaseContext(), EventDetailActivity.class);
                        goToDetailsIntent.putExtra("eventDate", eventList.get(position).getDate());
                        goToDetailsIntent.putExtra("eventDescription", eventList.get(position).getDescription());
                        goToDetailsIntent.putExtra("eventName", eventList.get(position).getName());
                        goToDetailsIntent.putExtra("eventLat", eventList.get(position).getLat());
                        goToDetailsIntent.putExtra("eventLon", eventList.get(position).getLon());
                        startActivity(goToDetailsIntent);
                    });
                    LinearLayoutManager eventsManager = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false);
                    RecyclerView eventRV = findViewById(R.id.eventsRV);
                    runOnUiThread(()->{
                        eventRV.setLayoutManager(eventsManager);
                        eventRV.setAdapter(eventsAdapter);
                    });

                    Log.i(TAG, "success ");
                },
                error -> {
                    Log.i(TAG, "error " + error);
                }
        );

    }
}