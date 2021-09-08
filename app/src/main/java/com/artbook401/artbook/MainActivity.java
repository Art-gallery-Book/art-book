package com.artbook401.artbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.User;
import com.artbook401.artbook.adapters.PostsAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "item";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;
    String userName;
    User currentUser;
    List<Post> postsList = new ArrayList<>();
    List<String> followingLists = new ArrayList<>();
    List<Post> filteredPosts = new ArrayList<>();
    PostsAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUser();


        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        findViewById(R.id.textView2).setOnClickListener(v ->
//        {
//            Intent intent = new Intent(getApplicationContext() , Profile.class);
//            startActivity(intent);
//        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_account){
            startActivity(new Intent(getApplicationContext() , Profile.class)) ;
        }
        if (id == R.id.nav_Add_Event){
            startActivity(new Intent(getApplicationContext() , AddEventActivity.class)) ;
        }
        if (id == R.id.nav_Events){
            startActivity(new Intent(getApplicationContext() , EventsActivity.class)) ;
        }
        if (id == R.id.users_nav){
            startActivity(new Intent(getApplicationContext() , Users.class)) ;
        }
        if (id == R.id.nav_logout){
            Amplify.Auth.signOut(
                    () -> {
                        Log.i("AuthQuickstart", "Signed out successfully");
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    },
                    error -> Log.e("AuthQuickstart", error.toString())
            );
        }
        return false;
    }
    public void getUserName(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        userName = sharedPreferences.getString("userName", "  ");
        Log.i(TAG, "getUserName: hello again :" + userName);

    }

    public void getUser(){
        getUserName();
        Amplify.API.query(ModelQuery.list(User.class , User.NAME.eq(userName)),
                success -> {
                    Log.i(TAG, "onCreate: hi queryyyyyyyyyyyyyyyyy" + success.getData());
                    for (User user:success.getData())
                    {
                       followingLists=user.getFollowing();
                       currentUser=user;

                    }
                    Log.i(TAG, "success ");
                    getPosts();
                },
                error->{ Log.i(TAG, "error " + error);}
        );

    }

    public void getPosts(){
        Amplify.API.query(ModelQuery.list(Post.class),
                success ->{
                    for (Post post:success.getData())
                    {
                        postsList.add(post);
                    }
                    filteringPosts();
                },
                error -> {
                    Log.i(TAG, "error " + error);
                }
                );
    }

    public void filteringPosts(){
        for(Post post: postsList){
            if(followingLists.contains(post.getUserId()) || post.getUserId().equals(currentUser.getId())){
                filteredPosts.add(post);
            }
        }
        myAdapter = new PostsAdapter(filteredPosts);
        LinearLayoutManager postsManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView postsRecycleView = findViewById(R.id.mainRV);
        runOnUiThread(() -> {
            postsRecycleView.setLayoutManager(postsManager);
            postsRecycleView.setAdapter(myAdapter);
        });
    }
}