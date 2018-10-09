package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.areaFragment, new Client_SignUp());
                    ft.commit();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    FragmentTransaction fx = getSupportFragmentManager().beginTransaction();
                    fx.replace(R.id.areaFragment, new Tech_SignUp());
                    fx.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sign_up);

        //  Client = (Button) findViewById(R.id.clientSignUp);
        // Tech = (Button) findViewById(R.id.techSignUp);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //  public void switchFragments(View V){
/*
        Client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(LoginActivity.this, "Hello TOAST", Toast.LENGTH_SHORT).show();

                 //  if(V == findViewById(R.id.clientSignUp)){
                       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.areaFragment, new Client_SignUp());
                        ft.commit();
                 //}


            }

        }); */
/*
        Tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(LoginActivity.this, "Hello TECHN", Toast.LENGTH_SHORT).show();
  //               if (V == findViewById(R.id.techSignUp)) {

               FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
               ft.replace(R.id.areaFragment, new Tech_SignUp());
                ft.commit();
    //            }
            }
        }); */

    }

    public static Intent newIntent (Context packageContext){
        Intent i = new Intent(packageContext, SignUpActivity.class);
        return i;
    }

}