package com.artbook401.artbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.User;
import com.artbook401.artbook.R;
import com.artbook401.artbook.adapters.EventsAdapter;
import com.artbook401.artbook.adapters.PostsAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    private static final String TAG ="EventsActivity" ;
    private EventsAdapter eventsAdapter;
private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


getEvents();
        eventsAdapter = new EventsAdapter(eventList, position ->{
        });
        LinearLayoutManager eventsManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView eventRV = findViewById(R.id.eventsRV);
            eventRV.setLayoutManager(eventsManager);
            eventRV.setAdapter(eventsAdapter);
    }
public void getEvents(){
eventList = new ArrayList<>();
    Amplify.API.query(ModelQuery.list(Event.class ),
            success -> {
//            currentUser=success.getData();
                Log.i(TAG, "onCreate: hi queryyyyyyyyyyyyyyyyy" + success.getData());
                for (Event event:success.getData())
                {
                   eventList.add(event);

                    Log.i(TAG, "onCreate: hi useeeeeeeeeer"+eventList);

                }
                Log.i(TAG, "success ");
            },
            error->{ Log.i(TAG, "error " + error);}
    );

}
}