package com.artbook401.artbook;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.File;


public class LoginActivity extends AppCompatActivity {
    private Boolean isUserEmpty = true;
    private Boolean isPasswordEmpty = true;
    private static final String TAG = "SIGNIN";
    private String currentUser = " ";
    private String userID = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        configAmplify();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

        try {
            currentUser = Amplify.Auth.getCurrentUser().getUsername();
            userID = Amplify.Auth.getCurrentUser().getUserId();
            preferenceEditor.putString("userName" ,currentUser);
            preferenceEditor.putString("userID" ,userID);
            Log.i(TAG, "onCreate: what haaaaaaaaaaaaaapen !!!" + currentUser);
            preferenceEditor.apply();

            Intent goToHome = new Intent(this, MainActivity.class);
            startActivity(goToHome);
        } catch (RuntimeException error) {
            Log.i("currentUser", "onCreate: " + error);

        }

        findViewById(R.id.btnSignIn).setEnabled(!isPasswordEmpty && !isUserEmpty);

        TextView userNameSignIn = findViewById(R.id.userNameSignIn);
        userNameSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //findViewById(R.id.btnSignIn).setEnabled(false);
                isUserEmpty = editable.toString().equals("");
                findViewById(R.id.btnSignIn).setEnabled(!isPasswordEmpty && !isUserEmpty);

            }
        });
        TextView passwordSignIn = findViewById(R.id.passwordSignIn);
        passwordSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isPasswordEmpty = editable.toString().equals("");

                findViewById(R.id.btnSignIn).setEnabled(!isPasswordEmpty && !isUserEmpty);
            }
        });

        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                String userName = ((TextView) findViewById(R.id.userNameSignIn)).getText().toString();
                String password = ((TextView) findViewById(R.id.passwordSignIn)).getText().toString();

                Amplify.Auth.signIn(
                        userName,
                        password,
                        result -> {
                            Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

                            preferenceEditor.putString("userName", userName);
                            preferenceEditor.putString("userID", userID);
                            preferenceEditor.apply();
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            Intent signInToHome = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(signInToHome);
                        },
                        error -> {
                            Log.e("AuthQuickstart", error.toString());
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                );
            }
        });

        findViewById(R.id.btnSignupSignIn).setOnClickListener(view -> {
            Intent signInToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(signInToSignUp);
        });

        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "SignInActivity");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "SignInActivity");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Page");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void configAmplify() {
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Tutorial", "Could not initialize Amplify", e);
        }
    }

}