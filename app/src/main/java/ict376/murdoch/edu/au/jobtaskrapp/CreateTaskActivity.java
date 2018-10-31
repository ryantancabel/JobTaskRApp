package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView LocationView, taskTitle, description, taskRate, deadline, GeoLocation, CurrentTimeStamp;
    //Constant variable for upload image.
    public static final int TAKE_PIC_REQUEST_CODE = 0;
    public static final int CHOOSE_PIC_REQUEST_CODE = 1;
    public static final int MEDIA_TYPE_IMAGE = 2;


    private String title, des, rateDuration;
    private int rate;
    protected Button LocationButton, TaskdeadLineButton;
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    private Date dateTimestamp = Calendar.getInstance().getTime();
    //Image Upload component
    protected Button mAddImageBtn;
    protected Button mUploadImageBtn;

    //media Uri object it's important to upload image to parse
    private Uri mMediaUri;


    int day, month, year;
    int finalday, finalmonth, finalyear;


    //for Spinner
    private Spinner taskType;
    private ParseObject tasks = new ParseObject("Task");


    //ParseObject imageUpload = new ParseObject("ImageUpload");

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
        //initialise the button for image upload
        mAddImageBtn = (Button) findViewById(R.id.btn_add);


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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationButton = (Button) findViewById(R.id.location_button);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMapLocation();

            }
        });


        //add listener to clcik
        mAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTaskActivity.this);
                builder.setTitle("Choose or Take Photo");
                builder.setPositiveButton("choose", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //upload image
                        Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        choosePictureIntent.setType("image/*");
                        startActivityForResult(choosePictureIntent, CHOOSE_PIC_REQUEST_CODE);
                    }
                });
                builder.setNegativeButton("Take Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //take photo
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        if (mMediaUri == null) {
                            Toast.makeText(getApplicationContext(), "Sorry there was an error! Try again.", Toast.LENGTH_LONG).show();
                        } else {
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                            startActivityForResult(takePicture, TAKE_PIC_REQUEST_CODE);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    public void myMapLocation() {

        // requesting permission to get user's location
        if (ActivityCompat.checkSelfPermission(CreateTaskActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateTaskActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateTaskActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


            if (location != null) {
                // if it isn't, save it to Back4App Dashboard
                ParseGeoPoint currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                tasks.put("Location", currentUserLocation);
                GeoLocation.setText(currentUserLocation.toString());
            } else {
                Toast.makeText(this, String.format("Location is Null"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                myMapLocation();
                break;
        }
    }

    public void addListenerOnSpinnerItemSelection(View view) {
        taskType = (Spinner) view.findViewById(R.id.taskType);
        taskType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void SaveButton_OnClick(View view) {

        if (taskTitle.getText() != null) {
            title = taskTitle.getText().toString();
            des = description.getText().toString();
            rate = Integer.valueOf(taskRate.getText().toString());
            rateDuration = taskType.getSelectedItem().toString();

        }


        tasks.put("Title", title);
        tasks.put("Description", des);
        tasks.put("TaskRate", rate);
        tasks.put("RateDuration", rateDuration);
        tasks.put("PostedWhen", dateTimestamp);


        //    ParseObject currentUser = ParseUser.getCurrentUser();
        //  tasks.put("UserPointer", ParseObject.createWithoutData("User", currentUser.getObjectId()));


//This two below lines execute no error but cant input data in the parse table
//        ParseUser currentUser = ParseUser.getCurrentUser();
        //      tasks.put("UserPointer", ParseObject.createWithoutData("User", "fV78vHAwtB"));


     //   ParseUser currentUser = ParseUser.getCurrentUser();
        //tasks.put("UserPointer", ParseUser.getCurrentUser().getObjectId());
        try {
            //Convert image to bytes for upload.
            byte[] fileBytes = FileHelper.getByteArrayFromFile(CreateTaskActivity.this, mMediaUri);
            if (fileBytes == null) {
                //there was an error
                Toast.makeText(getApplicationContext(), "There was an error, Try again!", Toast.LENGTH_LONG).show();
            } else {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
                String fileName = FileHelper.getFileName(CreateTaskActivity.this, mMediaUri, "image");
                final ParseFile file = new ParseFile(fileName, fileBytes);
                tasks.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {


                            //  tasks.saveInBackground();
                            //      updateObject();
                            // Notification();
                            tasks.put("Image", file);
                            tasks.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Toast.makeText(getApplicationContext(), "Success Uploading Image!", Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            //there was an error
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        } catch (Exception e1) {
            //the exception error will throw here.
            Toast.makeText(getApplicationContext(), e1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        tasks.saveInBackground();
        Notification();


    }

    public void Edit_Button_OnClick(View view) {
        Intent i = new Intent(CreateTaskActivity.this, EditTaskDetail.class);

        startActivity(i);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year2, int month2, int day2) {
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
        tasks.put("TaskWhen", deadlineDate);
        deadline.setText(deadlineDate.toString());

    }


    public void Notification() {

        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(CreateTaskActivity.this).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_menu_share)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_share))
                .setContentTitle("Notification: JobTasker")
                .setContentText("Your Job has been posted, someone will contact you for the job soon!");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());

    }


    /*    public void updateObject(){

            ParseUser currentUser = ParseUser.getCurrentUser();
            tasks.put("UserPointer", ParseObject.createWithoutData("User", currentUser.getObjectId()));
      */  //inner helper method
    private Uri getOutputMediaFileUri(int mediaTypeImage) {
        if (isExternalStorageAvailable()) {
            //get the URI
            //get external storage dir
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "UPLOADIMAGES");
            //create subirectory if it does not exit
            if (!mediaStorageDir.exists()) {
                //create dir
                if (!mediaStorageDir.mkdirs()) {
                    // Log.e("SNAP59ERROR","Failed to create directory");
                    return null;
                }
            }
            //create a file name
            //create file
            File mediaFile = null;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaTypeImage == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG" + timestamp + ".jpg");
            }
            //return file uri
            Log.d("UPLOADIMAGE", "FILE: " + Uri.fromFile(mediaFile));

            return Uri.fromFile(mediaFile);
        } else {
            return null;
        }
    }


    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;

        } else {
            return false;
        }
    }

    //this code needed to preview just in case if your application needed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PIC_REQUEST_CODE) {
                if (data == null) {
                    Toast.makeText(getApplicationContext(), "Image cannot be null", Toast.LENGTH_LONG).show();

                } else {
                    mMediaUri = data.getData();
                    //set previews
                    //mPreviewImageView.setImageURI(mMediaUri);
                }
            } else {

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
                //set previews
                //mPreviewImageView.setImageURI(mMediaUri);
            }
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
        }
    }
}

    /* testing for pointers   
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

