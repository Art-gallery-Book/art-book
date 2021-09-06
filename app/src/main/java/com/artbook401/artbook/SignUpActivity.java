package com.artbook401.artbook;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        onChooseFile(result.getData().getData());
                    }
                });

        Button addingPhotoBTN = findViewById(R.id.profileImageBTN);

        addingPhotoBTN.setOnClickListener(view -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("image/*");
            chooseFile= Intent.createChooser(chooseFile,"Choose An Image");
            someActivityResultLauncher.launch(chooseFile);
        });



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

        findViewById(R.id.btnSignup).setOnClickListener(view -> {
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), emailSignup.getText().toString())
                    .build();
            Amplify.Auth.signUp(userNameSignup.getText().toString(), passwordSignup.getText().toString(), options,
                    result -> {
                        Log.i("AuthQuickStart", "Result: " + result.toString());

                        Intent signUpToConfirm = new Intent(getApplicationContext(), ConfirmActivity.class);
                        signUpToConfirm.putExtra("userName", userNameSignup.getText().toString());
                        signUpToConfirm.putExtra("password", passwordSignup.getText().toString());
                        signUpToConfirm.putExtra("imageName",userImageFileName);
                        startActivity(signUpToConfirm);
                    },
                    error -> Log.e("AuthQuickStart", "Sign up failed", error)
            );
        });

        findViewById(R.id.btnSignInSignup).setOnClickListener(view -> {
            Intent signUpToSignIn = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(signUpToSignIn);
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onChooseFile(Uri uri){
        userImageFileName = new Date().toString() + ".png";
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