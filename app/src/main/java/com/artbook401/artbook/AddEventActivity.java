package com.artbook401.artbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    private static final String TAG ="addEvent" ;
    private EditText edittext;
    private final Calendar myCalendar = Calendar.getInstance();
    private  String userImageFileName;
    private  Double lat;
    private  Double lon;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        MapUtility.apiKey  = getResources().getString(R.string.api_key);
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
//        ActionBar actionBar = getSupportActionBar();
//
//        // showing the back button in action bar
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);

// You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed

//        ActivityResultLauncher<Intent> AddImageLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        assert result.getData() != null;
//                        onChooseFile(result.getData().getData());
//                    }
//                });
//
//        findViewById(R.id.addImageEventBtn).setOnClickListener(view -> {
//            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//            chooseFile.setType("image/*");
//            chooseFile= Intent.createChooser(chooseFile,"Choose An Image");
//            AddImageLauncher.launch(chooseFile);
//        });

        findViewById(R.id.addEventBTN).setOnClickListener(view ->{
            EditText eventName=findViewById(R.id.eventName);
            EditText eventDec=findViewById(R.id.eventDesc);
            EditText eventDate=findViewById(R.id.eventDate);

            Event newEvent=Event.builder()
                    .name(eventName.getText().toString())
                    .description(eventDec.getText().toString())
                    .date(eventDate.getText().toString())
                    .lat(lat)
                    .lon(lon)
                    .build();
            Amplify.API.mutate(ModelMutation.create(newEvent) ,
                    res -> Log.i(TAG, "silentSignIn: user create successfully"),
                    error -> Log.e(TAG, "silentSignIn: error" ));
        });

//        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            // There are no request codes
//                            Intent data = result.getData();
//                            pickLocation(data);
//                        }
//                    }
//                });

        findViewById(R.id.addLocationBTN).setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), LocationPickerActivity.class);
            startActivityForResult(intent, 1);
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

//            try {
////                startActivityForResult(builder.build(AddEventActivity.this), PLACE_PICKER_REQUEST);
//                someActivityResultLauncher.launch(builder.build(AddEventActivity.this));
//            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
        });

        edittext = findViewById(R.id.eventDate);
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edittext.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(AddEventActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode ==1) {
                try {
                    if (data !=null&& data.getStringExtra(MapUtility.ADDRESS) !=null) {
                        lat = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                        lon = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                        Bundle completeAddress =data.getBundleExtra("fullAddress");
                        TextView locationView = findViewById(R.id.eventLocation);
                        String latLon =lat+ " " + lon;
                        locationView.setText(latLon);
                        locationView.setVisibility(View.VISIBLE);
                        /* data in completeAddress bundle                    "fulladdress"                    "city"                    "state"                    "postalcode"                    "country"                    "addressline1"                    "addressline2"*/
//                        txtAddress.setText(newStringBuilder().append("addressline2: ").append
//                                (completeAddress.getString("addressline2")).append("\ncity: ").append
//                                (completeAddress.getString("city")).append("\npostalcode: ").append
//                                (completeAddress.getString("postalcode")).append("\nstate: ").append
//                                (completeAddress.getString("state")).toString());
//
//                        txtLatLong.setText(newStringBuilder().append("Lat:").append(currentLatitude).append
//                                ("  Long:").append(currentLongitude).toString());

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//       onBackPressed();
//        return super.onSupportNavigateUp();
//    }
//    private void pickLocation(Intent data){
//        Place place = PlacePicker.getPlace(data, this);
//        String toastMsg = String.format("Place: %s", place.getName());
//
//        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//
//        TextView locationView = findViewById(R.id.eventLocation);
//        lat = place.getLatLng().latitude;
//        lon = place.getLatLng().longitude;
//        String latLon =lat+ " " + lon;
//        locationView.setText(latLon);
//        locationView.setVisibility(View.VISIBLE);
//    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

//    @RequiresApi(api = Build.VERSION_CODES.Q)
//
//    public void onChooseFile(Uri uri){
//        userImageFileName = new Date().toString()+".png";
//        File uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");
//
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
//        } catch (Exception exception) {
//            Log.e("onChooseFile", "onActivityResult: file upload failed" + exception.toString());
//        }
//
//        Amplify.Storage.uploadFile(
//                userImageFileName,
//                uploadFile,
//                success -> {
//                    Log.i("onChooseFile", "uploadFileToS3: succeeded " + success.getKey());
//                    Toast.makeText(getApplicationContext(), "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();
//
//                },
//                error -> {
//                    Log.e("onChooseFile", "uploadFileToS3: failed " + error.toString());
//                    Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_SHORT).show();
//
//                }
//        );
//
//    }


}

