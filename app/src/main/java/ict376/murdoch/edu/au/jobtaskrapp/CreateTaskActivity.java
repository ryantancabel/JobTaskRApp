package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CreateTaskActivity extends AppCompatActivity {

    private TextView taskTitle, description;
    private String title, des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this);
        setContentView(R.layout.activity_create_task);

        Intent i = getIntent();

        taskTitle=(TextView)findViewById(R.id.ed_TaskTitle);
        description=(TextView)findViewById(R.id.ed_Description);
    }

    public void SaveButton_OnClick(View view)
    {

        if(taskTitle.getText() != null) {
            title = taskTitle.getText().toString();
            des =description.getText().toString();
        }
        ParseObject tasks = new ParseObject("Task");
        tasks.put("Title",title);
        tasks.put("Description",des);
        tasks.saveInBackground();

    }
    public void Edit_Button_OnClick(View view) {
        Intent i = new Intent(CreateTaskActivity.this, EditTaskDetail.class);

        startActivity(i);
    }
}
