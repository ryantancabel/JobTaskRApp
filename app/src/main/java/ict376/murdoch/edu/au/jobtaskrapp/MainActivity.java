package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseInstallation;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this);
        setContentView(R.layout.activity_main);

        final Button login_button = (Button) findViewById(R.id.login_button);
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(MainActivity.this, NxtActivity.class);
                            startActivity(i);

                        } else {
                            Toast.makeText(MainActivity.this, "No", Toast.LENGTH_SHORT).show();
                            ParseUser.logOut();
                        }
                    }

                });
            }
        });

        //save current installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
