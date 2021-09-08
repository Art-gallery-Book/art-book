package com.artbook401.artbook;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfirmActivity extends AppCompatActivity {
    private static final String TAG ="ConfirmActivity" ;
    private Boolean isCodeEmpty = true;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent intent = getIntent();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        findViewById(R.id.btnVerificationCode).setEnabled(!isCodeEmpty);

        TextView confirmCode = findViewById(R.id.verificationCodeInput);
        confirmCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isCodeEmpty = editable.toString().equals("");

                findViewById(R.id.btnVerificationCode).setEnabled(!isCodeEmpty);
            }
        });

        findViewById(R.id.btnVerificationCode).setOnClickListener(view -> {
            Amplify.Auth.confirmSignUp(
                    intent.getStringExtra("userName"),
                    confirmCode.getText().toString(),
                    result -> {
                        Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                        silentSignIn(intent.getStringExtra("userName"),intent.getStringExtra("password"),intent.getStringExtra("imageName"), preferences);
                    },
                    error -> Log.e("AuthQuickstart", error.toString())
            );
        });

        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConfirmSignUpActivity");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConfirmSignUpActivity");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Page");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void silentSignIn(String userName, String password,String imageName,SharedPreferences preferences){
        List<String> test=new ArrayList<>();
        User newUser = User.builder().name(userName).profileImage("profile1.jpg").following(test).build();

        Amplify.API.mutate(ModelMutation.create(newUser) ,
        res -> Log.i(TAG, "silentSignIn: user create successfully"),
        error -> Log.e(TAG, "silentSignIn: error" ));

        Amplify.Auth.signIn(
                userName,
                password,
                result -> {
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor preferenceEditor = preferences.edit();
                    preferenceEditor.putString("userName", userName);
                    Intent signInToHome = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(signInToHome);
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }

}