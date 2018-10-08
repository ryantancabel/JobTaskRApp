package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class NxtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nxt_activity);
        Intent i = getIntent();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.placeholder);

        if (fragment == null) {
            fragment = new NxtFragment();
            fm.beginTransaction()
                    .add(R.id.placeholder, fragment)
                    .commit();
        }
    }

    public void pressedNextButton(View view) {

        Intent i = new Intent(NxtActivity.this, CreateTaskActivity.class);
        startActivity(i);

    }
}
