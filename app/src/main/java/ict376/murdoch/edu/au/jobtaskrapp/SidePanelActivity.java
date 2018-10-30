package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

public class SidePanelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener
{
    //Need this for bottom navigation view Fragment switching
    TaskListFragment  tkfg = null;
    HelpFragment helpfg = null;
    protected boolean flag = false;
    //end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent i = getIntent();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.taskListPlaceholder);

        if (fragment == null) {
            fragment = new TaskListFragment();
            fm.beginTransaction().add(R.id.taskListPlaceholder, fragment).commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.taskListPlaceholder);

            if (fragment == null) {
                fragment = new TaskListFragment();
                fm.beginTransaction().add(R.id.taskListPlaceholder, fragment).commit();
            }
        } else if (id == R.id.nav_gallery) {

        } else */
        if (id == R.id.help){

            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
            String help = getString(R.string.Help_Fragment);
            alerBuilder.setMessage(help);
            alerBuilder.setTitle("App User Guide");
            alerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertbox = alerBuilder.create();
            alertbox.show();

        } else if (id == R.id.nav_manage) {

            ParseUser.logOut();
            Toast.makeText(this, String.format("Loged out"), Toast.LENGTH_SHORT).show();
            Intent ProfilePage = new Intent(this, MainActivity.class);
            startActivity(ProfilePage);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent newIntent (Context packageContext){
        Intent i = new Intent(packageContext, SidePanelActivity.class);
        return i;
    }


    public void pressedNextButton(View view) {

        Intent i = new Intent(SidePanelActivity.this, CreateTaskActivity.class);
        startActivity(i);

    }

//FOR INTERNAL NOTIFICATION FUNCTION USED FOR FLOATING BUTTON
    public void Notification(){

        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(SidePanelActivity.this).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_menu_share)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_menu_share))
                .setContentTitle("Notification: JobTasker")
                .setContentText("Your Are using the floating button");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());

    }


    //Bottom Navigation working fine, at the moment using HelpFragment to display ImageView to say under-construction

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_home:
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        // Fragment currentFragment = getApplicationContext().getFragmentManager().findFragmentById(R.id.fragment_container);
                        tkfg = new TaskListFragment();
                        ft.replace(R.id.taskListPlaceholder, tkfg);
                        ft.commit();
                        flag = true;
                        break;
                    case R.id.navigation_complete:
                        FragmentTransaction fx = getSupportFragmentManager().beginTransaction();
                        helpfg = new HelpFragment();
                        fx.replace(R.id.taskListPlaceholder, helpfg);
                        fx.commit();
                        flag = true;
                        break;
                }

                return flag;
            }
        };
    }

}
