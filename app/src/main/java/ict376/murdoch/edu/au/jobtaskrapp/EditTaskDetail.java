package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditTaskDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView taskTitle, description, taskRate;
    private String title, des,rateDuration;
    private int rate;

    //declare button
    private Button uploadpic;
    private static final int CAMERA_REQUEST = 1888;

    TaskDataModel taskObject;
    String id;


    //for SpiRRnner
    private Spinner taskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_detail);
        Parse.initialize(this);

        Intent i = getIntent();

        Bundle data = i.getExtras();
        taskObject = i.getParcelableExtra("object");

        taskTitle=(TextView)findViewById(R.id.ed_TaskTitle);
        taskTitle.setText(taskObject.getTaskName());
        description=(TextView)findViewById(R.id.ed_Description);
        description.setText(taskObject.getTaskDescp());
        taskRate = (TextView)findViewById(R.id.taskRate);
        taskRate.setText(String.format("%.0f",taskObject.getTaskRate()));
        taskType = (Spinner)findViewById(R.id.taskType);

        Log.d("nnn", "onCreate: " + id);

        //initialise button
        uploadpic = (Button) findViewById(R.id.bt_uploadPicture);
        //add listener for the upload image button
        uploadpic.setOnClickListener(this);

        //add listener On Spinner
        addListenerOnSpinnerItemSelection(taskType);

        queryFromDataBase();
    }

    public void addListenerOnSpinnerItemSelection(View view)
    {
        taskType = (Spinner) view.findViewById(R.id.taskType);
        taskType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    //retrieve data from back4app database;
    public void queryFromDataBase() {
        TaskDataModel mTaskData = new TaskDataModel();
        //parse query connect to your table
        ParseQuery<ParseObject> query =  ParseQuery.getQuery("Task");

        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                // row of Object Id "U8mCwTHOaC"
                if (e == null)
                    for(ParseObject task : objects) {
                        taskTitle.setText(task.getString("Title"));
                        description.setText(task.getString("Description"));
                        taskRate.setText(String.valueOf(task.getInt("TaskRate")));


                        //Spinner set value coming from database
                        String rateValue = task.getString("RateDuration");

                        int position = 0;
                        switch (rateValue) {
                            case "Hourly":
                                position=0;
                                break;
                            case "Daily":
                                position=1;
                                break;
                            case "Monthly":
                                position=2;
                        }
                   //     Toast.makeText(getApplicationContext(), "This position is: "+position, Toast.LENGTH_SHORT).show();
                        taskType.setSelection(position);
                    }
                else {
                    // error
                }
            }
        });
    }

    public void SaveButton_OnClick(View view)
    {

        if(taskTitle.getText() != null) {
            title = taskTitle.getText().toString();
            des =description.getText().toString();
            rate = Integer.valueOf( taskRate.getText().toString());
            rateDuration = taskType.getSelectedItem().toString();
        }

        //update table by id
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject task = list.get(0);
                    task.put("Title", title);
                    task.put("Description",des);
                    task.put("TaskRate",rate);
                    task.put("RateDuration", rateDuration);
                    task.saveInBackground();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        //Show Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload or Take a Photo");
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //upload image function here
            }
        });
        builder.setNegativeButton("Take Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //take photo function here
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } //use camera end

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
