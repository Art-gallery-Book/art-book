package com.artbook401.artbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUpActivity extends AppCompatActivity {
    private Boolean isUserEmpty = true;
    private Boolean isPasswordEmpty = true;
    private Boolean isEmailEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty);

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
                findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty);

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

                findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty);
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

                findViewById(R.id.btnSignup).setEnabled(!isPasswordEmpty && !isUserEmpty && !isEmailEmpty);
            }
        });

//        findViewById(R.id.btnSignup).setOnClickListener(view -> {
//            AuthSignUpOptions options = AuthSignUpOptions.builder()
//                    .userAttribute(AuthUserAttributeKey.email(), emailSignup.getText().toString())
//                    .build();
//            Amplify.Auth.signUp(userNameSignup.getText().toString(), passwordSignup.getText().toString(), options,
//                    result -> {
//                        Log.i("AuthQuickStart", "Result: " + result.toString());
//
//                        Intent signUpToConfirm = new Intent(getApplicationContext(), ConfirmSignUpActivity.class);
//                        signUpToConfirm.putExtra("userName", userNameSignup.getText().toString());
//                        signUpToConfirm.putExtra("password", passwordSignup.getText().toString());
//                        startActivity(signUpToConfirm);
//                    },
//                    error -> Log.e("AuthQuickStart", "Sign up failed", error)
//            );
//        });

//        findViewById(R.id.btnSignInSignup).setOnClickListener(view -> {
//            Intent signUpToSignIn = new Intent(getApplicationContext(), SignInActivity.class);
//            startActivity(signUpToSignIn);
//        });
    }
}