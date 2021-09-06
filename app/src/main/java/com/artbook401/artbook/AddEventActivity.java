package com.artbook401.artbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    private EditText edittext;
    private final Calendar myCalendar = Calendar.getInstance();
    private int PLACE_PICKER_REQUEST = 1;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("geo:37.7749,-122.4194"));
//        startActivity(intent);
// You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            pickLocation(data);
                        }
                    }
                });

        findViewById(R.id.addLocationBTN).setOnClickListener(view ->{
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
//                startActivityForResult(builder.build(AddEventActivity.this), PLACE_PICKER_REQUEST);
                someActivityResultLauncher.launch(builder.build(AddEventActivity.this));
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
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

    private void pickLocation(Intent data){
        Place place = PlacePicker.getPlace(data, this);
        String toastMsg = String.format("Place: %s", place.getName());

        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

        TextView locationView = findViewById(R.id.eventLocation);
        String latLon =place.getLatLng().latitude + " " + place.getLatLng().longitude;
        locationView.setText(latLon);
        locationView.setVisibility(View.VISIBLE);
    }

    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, this);
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }
}