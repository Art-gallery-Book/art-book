package com.artbook401.artbook;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.User;
import com.artbook401.artbook.adapters.PostsAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Profile extends AppCompatActivity {
    private static final String TAG ="success" ;
    String userName = " ";
    String userImageFileName="";
    User currentUser;
    List <Post> postsList = new ArrayList<>();
    private PostsAdapter postsAdapter;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        getUserName();
        getUser();


        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        onChooseFile(result.getData().getData());
                    }
                });



        findViewById(R.id.submitPost).setOnClickListener(view ->{
            EditText post=findViewById(R.id.postDesc);
            Post newPost=Post.builder()
                    .image(userImageFileName)
                    .userId(currentUser.getId())
                    .body(post.getText().toString())
                    .build();

            Amplify.API.mutate(ModelMutation.create(newPost) ,
                    res -> {
                        Log.i(TAG, "silentSignIn: user create successfully");
                        getUser();
                    },
                    error -> Log.e(TAG, "silentSignIn: error" ));
        });

        List<String> list=new ArrayList<>();
        list.add("dfb6b0a8-b904-4847-8ddf-1c55cf6e4a2f");
//        Log.i("kkkkkkkkk",currentUser.getId());
User user = User.builder().name("ashqur").id("fb577a0f-77b2-49e9-8bcf-78d79352f031").following(list)
        .build();
        Amplify.API.mutate(ModelMutation.update(user),
                response -> Log.i("MyAmplifyApp", "Todo with id: " + response.getData().getId()),
                error -> Log.e("MyAmplifyApp", "Create failed", error)
        );

            Button addingPhotoBTN = findViewById(R.id.postImageBTN);

        ((TextView) findViewById(R.id.userNameText)).setText(userName);
//        ((ImageView) findViewById(R.id.userImageView)).setImageBitmap();


        addingPhotoBTN.setOnClickListener(view -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("image/*");
            chooseFile= Intent.createChooser(chooseFile,"Choose An Image");
            someActivityResultLauncher.launch(chooseFile);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserName();

    }

    public void getUserName(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        userName = sharedPreferences.getString("userName" , "  ");
        Log.i(TAG, "getUserName: hello again :" + userName);

    }


    // uploading profile photo to S3:

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onChooseFile(Uri uri){
        userImageFileName = new Date().toString()+".png";
        File uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
        } catch (Exception exception) {
            Log.e("onChooseFile", "onActivityResult: file upload failed" + exception.toString());
        }

        Amplify.Storage.uploadFile(
                userImageFileName,
                uploadFile,
                success -> {
                    Log.i("onChooseFile", "uploadFileToS3: succeeded " + success.getKey());
                    Toast.makeText(getApplicationContext(), "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();

                },
                error -> {
                    Log.e("onChooseFile", "uploadFileToS3: failed " + error.toString());
                    Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_SHORT).show();

                }
        );

    }

    public void getUser(){
        Amplify.API.query(ModelQuery.list(User.class , User.NAME.eq(userName)),
                success -> {
//            currentUser=success.getData();
                    Log.i(TAG, "onCreate: hi queryyyyyyyyyyyyyyyyy" + success.getData());
                    for (User user:success.getData())
                    {
                        currentUser=user;
//                        postsList.addAll(user.getPosts());
                        postsAdapter = new PostsAdapter(user.getPosts());
                        LinearLayoutManager postsManager = new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false);
                        RecyclerView postsRecycleView = findViewById(R.id.postsRV);
                        runOnUiThread(() -> {
                            postsRecycleView.setLayoutManager(postsManager);
                            postsRecycleView.setAdapter(postsAdapter);
                        });

                        Log.i(TAG, "onCreate: hi useeeeeeeeeer");

                    }
                    Log.i(TAG, "success ");
                },
                error->{ Log.i(TAG, "error " + error);}
        );

    }




}