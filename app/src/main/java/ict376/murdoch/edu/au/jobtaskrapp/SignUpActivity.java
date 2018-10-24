package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private View V;
    protected boolean flag = false;

    Client_SignUp  myFg = null;
    Tech_SignUp   newTechSignupFg = null;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.client_Nav:
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        // Fragment currentFragment = getApplicationContext().getFragmentManager().findFragmentById(R.id.fragment_container);
                        if (newTechSignupFg != null) {
                            ft.remove(newTechSignupFg);

                          //  ft.commit();
                        }
                        myFg = new Client_SignUp();
                        ft.replace(R.id.areaFragment, myFg);
                       // ft.addToBackStack(null);
                        ft.commit();
                        flag = true;
                        break;
                    case R.id.tech_Nav:
                        FragmentTransaction fx = getSupportFragmentManager().beginTransaction();
                        newTechSignupFg = new Tech_SignUp();
                        fx.replace(R.id.areaFragment, newTechSignupFg);
                     //   fx.addToBackStack(null);
                        fx.commit();
                        flag = true;
                        break;
                }

                return flag;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, SignUpActivity.class);
        return i;
    }




}