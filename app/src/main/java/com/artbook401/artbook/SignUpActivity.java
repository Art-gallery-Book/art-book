package com.artbook401.artbook;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    private Boolean isUserEmpty = true;
    private Boolean isPasswordEmpty = true;
    private Boolean isEmailEmpty = true;
    private  String userImageFileName;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

        findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty );

        TextView userNameSignup = findViewById(R.id.userNameSignup);
        userNameSignup.addTextChangedListener(new TextWatcher() {
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
                findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty );

            }
        });
        TextView passwordSignup = findViewById(R.id.passwordSignup);
        passwordSignup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isPasswordEmpty = editable.toString().equals("");

                findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty );
            }
        });

        TextView emailSignup = findViewById(R.id.emailSignup);
        emailSignup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEmailEmpty = editable.toString().equals("");

                findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty );
            }
        });

        findViewById(R.id.btnSignup).setOnClickListener(view -> {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), emailSignup.getText().toString())
                    .build();
            Amplify.Auth.signUp(userNameSignup.getText().toString(), passwordSignup.getText().toString(), options,
                    result -> {
                        Log.i("AuthQuickStart", "Result: " + result.toString());
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        Intent signUpToConfirm = new Intent(getApplicationContext(), ConfirmActivity.class);
                        signUpToConfirm.putExtra("userName", userNameSignup.getText().toString());
                        signUpToConfirm.putExtra("password", passwordSignup.getText().toString());
                        signUpToConfirm.putExtra("imageName",userImageFileName);
                        startActivity(signUpToConfirm);
                    },
                    error -> {
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        Log.e("AuthQuickStart", "Sign up failed", error);
                    }
            );
        });

        findViewById(R.id.btnSignInSignup).setOnClickListener(view -> {
            Intent signUpToSignIn = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(signUpToSignIn);
        });
    }

}