package ict376.murdoch.edu.au.jobtaskrapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this);

        //save current installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    /*
    login_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ParseUser.logInInBackground("Username", "Password", new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null) {
                        //Login Successful
                        //You may choose what to do or display here
                        //For example: Welcome + ParseUser.getUsername()

                    } else {
                        //Login Fail
                        //get error by calling e.getMessage()
                    }
                }
            });
        }
    } */
}
