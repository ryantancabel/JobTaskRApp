package ict376.murdoch.edu.au.jobtaskrapp;

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


    protected Button Client;
    protected Button Tech;
    private View V;
    private TextView mTextMessage;
    protected boolean flag = false;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.client_Nav:
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.areaFragment, new Client_SignUp());
                        ft.commit();
                        flag = true;
                        break;
                    case R.id.tech_Nav:
                        FragmentTransaction fx = getSupportFragmentManager().beginTransaction();
                        fx.replace(R.id.areaFragment, new Tech_SignUp());
                        fx.commit();
                        flag = true;
                        break;


                }
                /*

                 if(item.getItemId() == R.id.client_Nav) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.areaFragment, new Client_SignUp());
                        ft.commit();
                        flag = true;
                    }else if(item.getItemId() == R.id.tech_Nav) {

                        FragmentTransaction fx = getSupportFragmentManager().beginTransaction();
                        fx.replace(R.id.areaFragment, new Tech_SignUp());
                        fx.commit();
                        flag = true;
                    }else{

                        Toast.makeText(SignUpActivity.this, getString(R.string.testing), Toast.LENGTH_SHORT).show();


                        flag = false;
                    }

                 */

                return flag;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sign_up);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public static Intent newIntent (Context packageContext){
        Intent i = new Intent(packageContext, SignUpActivity.class);
        return i;
    }

}