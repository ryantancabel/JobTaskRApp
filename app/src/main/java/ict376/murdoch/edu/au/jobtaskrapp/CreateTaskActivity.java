package ict376.murdoch.edu.au.jobtaskrapp;

import android.text.format.DateFormat;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.Calendar;
import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView LocationView, taskTitle, description, taskRate, deadline;
    private String title, des, rateDuration;
    private int rate;
    protected Button LocationButton, TaskdeadLineButton;
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;


    int day, month, year;
    int finalday, finalmonth, finalyear;


    //for SpiRRnner
    private Spinner taskType;
    ParseObject tasks = new ParseObject("Task");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this);
        setContentView(R.layout.activity_create_task);

        Intent i = getIntent();

        taskTitle = (TextView) findViewById(R.id.ed_TaskTitle);
        description = (TextView) findViewById(R.id.ed_Description);
        taskRate = (TextView) findViewById(R.id.taskRate);
        taskType = (Spinner) findViewById(R.id.taskType);
        TaskdeadLineButton = (Button) findViewById(R.id.taskDeadline);

        TaskdeadLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dataPickerDialog = new DatePickerDialog(CreateTaskActivity.this, CreateTaskActivity.this, year, month, day);
                dataPickerDialog.show();
            }
        });


        //add listener On Spinner
        addListenerOnSpinnerItemSelection(taskType);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationButton = (Button) findViewById(R.id.location_button);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMapLocation();

            }
        });

        }

    public void myMapLocation(){

            // requesting permission to get user's location
            if(ActivityCompat.checkSelfPermission(CreateTaskActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateTaskActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CreateTaskActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
            else {

                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                if(location != null){
                    // if it isn't, save it to Back4App Dashboard
                    ParseGeoPoint currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                    tasks.put("Location", currentUserLocation);
                }
                else {
                    Toast.makeText(this, String.format("Location is Null"), Toast.LENGTH_SHORT).show();
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                myMapLocation();
                break;
        }
    }

    public void addListenerOnSpinnerItemSelection(View view)
    {
        taskType = (Spinner) view.findViewById(R.id.taskType);
        taskType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void SaveButton_OnClick(View view)
    {

        if(taskTitle.getText() != null) {
            title = taskTitle.getText().toString();
            des =description.getText().toString();
            rate = Integer.valueOf( taskRate.getText().toString());
            rateDuration = taskType.getSelectedItem().toString();

        }


        tasks.put("Title",title);
        tasks.put("Description",des);
        tasks.put("TaskRate",rate);
        tasks.put("RateDuration", rateDuration);
        Date dateTimestamp = Calendar.getInstance().getTime();
        tasks.put("PostedWhen",dateTimestamp);

        // String currentUser = ParseUser.getCurrentUser().getObjectId();

        //tasks.put("UserPointer", ParseUser.getCurrentUser().getObjectId());

        tasks.saveInBackground();

    }
    public void Edit_Button_OnClick(View view) {
        Intent i = new Intent(CreateTaskActivity.this, EditTaskDetail.class);

        startActivity(i);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year2, int month2, int day2 ) {
        finalday = day2;
        finalmonth = month2;
        finalyear = year2;

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Date deadlineDate = Calendar.getInstance().getTime();
        deadlineDate.setDate(finalday);
        deadlineDate.setMonth(finalmonth);
        deadlineDate.setYear(finalyear);
        tasks.put("TaskWhen",deadlineDate);

    }


    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }


}
