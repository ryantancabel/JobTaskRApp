package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;


public class NextActivity extends ListActivity {

    private String tid,tname,tdescp;


    private ArrayList<TaskDataModel> dataModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Intent i = getIntent();

        dataModelList = new ArrayList<TaskDataModel>();

        ArrayAdapter<TaskDataModel> adapter = new ArrayAdapter<TaskDataModel>(this, R.layout.list_item_layout, dataModelList);

        setListAdapter(adapter);

        refreshTaskDataList();

    }

    private void refreshTaskDataList() {

        //parse query connect to your table
        ParseQuery<ParseObject> query =  ParseQuery.getQuery("Task");

        //Sort by created at or can be used updated At column in the parse table
        query.orderByAscending("_created_at");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null)
                {
                    dataModelList.clear();
                    for(ParseObject task : taskList)
                    {
                        TaskDataModel mtaskdata = new TaskDataModel(task.getString("Title"), task.getString("Description"));

                        dataModelList.add(mtaskdata);
                    }

                    ((ArrayAdapter<TaskDataModel>)getListAdapter()).notifyDataSetChanged();
                }
                else
                {
                    Log.d(getClass().getSimpleName(),"Error:" + e.getMessage());
                }
            }
        });
    }

    // func to call when the user tap the next button on the MainActivity
    public void pressedNextButton(View v)
    {
        Intent i = new Intent(NextActivity.this, CreateTaskActivity.class);
        Button btnNext = (Button) findViewById(R.id.btn_Next);
        startActivity(i);

    }


}
