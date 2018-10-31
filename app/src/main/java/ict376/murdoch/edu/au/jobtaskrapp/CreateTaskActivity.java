package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
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

    private TextView LocationView, taskTitle, description, taskRate, deadline, GeoLocation, CurrentTimeStamp;
    private String title, des, rateDuration;
    private int rate;
    protected Button LocationButton, TaskdeadLineButton;
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    private Date dateTimestamp = Calendar.getInstance().getTime();
    int day, month, year;
    int finalday, finalmonth, finalyear;

    //for Spinner
    private Spinner taskType;
    private ParseObject tasks = new ParseObject("Task");


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
        deadline = (TextView) findViewById(R.id.Deadline);
        GeoLocation = (TextView) findViewById(R.id.Location);
        CurrentTimeStamp = (TextView) findViewById(R.id.CurrentTimeStamp);


        CurrentTimeStamp.setText(dateTimestamp.toString());

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
                    GeoLocation.setText(currentUserLocation.toString());
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
        tasks.put("PostedWhen",dateTimestamp);


    //    ParseObject currentUser = ParseUser.getCurrentUser();
      //  tasks.put("UserPointer", ParseObject.createWithoutData("User", currentUser.getObjectId()));


//This two below lines execute no error but cant input data in the parse table
//        ParseUser currentUser = ParseUser.getCurrentUser();
  //      tasks.put("UserPointer", ParseObject.createWithoutData("User", "fV78vHAwtB"));


/*         ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null) {
            tasks.put("UserPointer", currentUser.getObjectId());//getParseObject("objectId"));
            Toast.makeText(this, String.format("This is" + currentUser.getObjectId()), Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, String.format("Their is no current user"), Toast.LENGTH_SHORT).show();

        }
*/
        tasks.saveInBackground();
  //      updateObject();
        Notification();
     /*
        Intent FeedPage= new Intent(CreateTaskActivity.this, SidePanelActivity.class);
        startActivity(FeedPage); */
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
        deadline.setText(deadlineDate.toString());

    }


    public void Notification(){

        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(CreateTaskActivity.this).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_menu_share)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_menu_share))
                .setContentTitle("Notification: JobTasker")
                .setContentText("Your Job has been posted, someone will contact you for the job soon!");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());

    }

/*
    public void updateObject(){

        ParseUser currentUser = ParseUser.getCurrentUser();
        tasks.put("UserPointer", ParseObject.createWithoutData("User", currentUser.getObjectId()));

        if(currentUser != null) {
          //  tasks.put("UserPointer", currentUser.getObjectId());//getParseObject("objectId"));
           // Toast.makeText(this, String.format("Their is " + currentUser.getObjectId()), Toast.LENGTH_SHORT).show();

            tasks.put("UserPointer", ParseObject.createWithoutData("User", currentUser.getObjectId()));

            tasks.saveInBackground();
        }else{
            Toast.makeText(this, String.format("Their is no current user"), Toast.LENGTH_SHORT).show();

        }

    }
*/
}
