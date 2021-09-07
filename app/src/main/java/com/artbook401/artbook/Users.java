package com.artbook401.artbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.artbook401.artbook.adapters.UsersAdapter;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity {
    private static final String TAG = "getting users";
    List<User> users;
    UsersAdapter usersAdapter;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        getUsers();

    }

    public void getUsers() {
        users = new ArrayList<>();
        Amplify.API.query(ModelQuery.list(User.class),
                success -> {
                    Log.i(TAG, "onCreate: hi queryyyyyyyyyyyyyyyyy" + success.getData());
                    for (User user : success.getData()) {
                        users.add(user);

                        Log.i(TAG, "onCreate: hi useeeeeeeeeer" + users);

                    }
                    usersAdapter = new UsersAdapter(users);
                    LinearLayoutManager usersManager = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false);
                    RecyclerView usersRV = findViewById(R.id.usersRV);
                    runOnUiThread(()->{
                        usersRV.setLayoutManager(usersManager);
                        usersRV.setAdapter(usersAdapter);
                    });

                    Log.i(TAG, "success ");
                },
                error -> {
                    Log.i(TAG, "error " + error);
                }
        );

    }


}