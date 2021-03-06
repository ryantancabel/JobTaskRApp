package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v4.app.FragmentTransaction;

public class TaskSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();

            setContentView(R.layout.activity_task_search);

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.taskListPlaceholder);

            if (fragment == null) {
                fragment = new TaskListFragment();
                fm.beginTransaction()
                        .add(R.id.taskListPlaceholder, fragment)
                        .commit();
            }

        }

    public void pressedNextButton(View view) {

        Intent i = new Intent(TaskSearchActivity.this, CreateTaskActivity.class);
        startActivity(i);

    }
}
