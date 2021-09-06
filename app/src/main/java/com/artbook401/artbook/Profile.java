package com.artbook401.artbook;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.User;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        getUserName();

        Amplify.API.query(ModelQuery.list(User.class , User.NAME.eq(userName)),
                success -> {
//            currentUser=success.getData();
                    Log.i(TAG, "onCreate: hi queryyyyyyyyyyyyyyyyy" + success.getData());
                    for (User user:success.getData())
                    {

                        currentUser=user;
                        Log.i(TAG, "onCreate: hi useeeeeeeeeer");

                    }
                    Log.i(TAG, "success ");
                },
                error->{ Log.i(TAG, "error " + error);}
        );
        

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
                    res -> Log.i(TAG, "silentSignIn: user create successfully"),
                    error -> Log.e(TAG, "silentSignIn: error" ));
        });



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

}