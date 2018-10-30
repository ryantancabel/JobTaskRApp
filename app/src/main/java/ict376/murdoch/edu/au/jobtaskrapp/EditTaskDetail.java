package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class EditTaskDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView taskTitle, description, taskRate;
    private String title, des,rateDuration;
    private int rate;
    //   private Object image;
    ArrayList<TaskDataModel> dataModelList = new ArrayList<>();
    String id = "R4vyTBkoW9";

    //for SpiRRnner
    private Spinner taskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_detail);
        Parse.initialize(this);
        Intent i = getIntent();

        taskTitle=(TextView)findViewById(R.id.ed_TaskTitle);
        description=(TextView)findViewById(R.id.ed_Description);
        taskRate = (TextView)findViewById(R.id.taskRate);
        taskType = (Spinner)findViewById(R.id.taskType);
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
}
