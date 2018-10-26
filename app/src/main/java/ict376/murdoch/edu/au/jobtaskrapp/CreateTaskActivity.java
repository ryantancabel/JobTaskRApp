package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CreateTaskActivity extends AppCompatActivity {

    private TextView taskTitle, description,taskRate;
    private String title, des,rateDuration;
    private int rate;
    //for SpiRRnner
    private Spinner taskType;
    //for notification
    private final String CHANNEL_ID = "accept_notification";
    private final  int NOTIFICATION_ID = 001;
    String CHANNEL_NAME = "Channel Name";// The public name of the channel.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this);
        setContentView(R.layout.activity_create_task);

        Intent i = getIntent();

        taskTitle=(TextView)findViewById(R.id.ed_TaskTitle);
        description=(TextView)findViewById(R.id.ed_Description);
        taskRate = (TextView)findViewById(R.id.taskRate);
        taskType = (Spinner)findViewById(R.id.taskType);
        //add listener On Spinner
        addListenerOnSpinnerItemSelection(taskType);

    }
    public void addListenerOnSpinnerItemSelection(View view)
    {
        taskType = (Spinner) view.findViewById(R.id.taskType);
        taskType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void SaveButton_OnClick(View view)
    {

        if(taskTitle.getText() != null) {
            title = taskTitle.getText().toString();
            des =description.getText().toString();
            rate = Integer.valueOf( taskRate.getText().toString());
            rateDuration = taskType.getSelectedItem().toString();
        }
        ParseObject tasks = new ParseObject("Task");
        tasks.put("Title",title);
        tasks.put("Description",des);
        tasks.put("TaskRate",rate);
        tasks.put("RateDuration", rateDuration);
        tasks.saveInBackground();

    }
    public void Edit_Button_OnClick(View view) {
        Intent i = new Intent(CreateTaskActivity.this, EditTaskDetail.class);

        startActivity(i);
    }
    private NotificationManager notifManager;

    public void Notification_OnClick(View view)
    {
        createNotification("Notification", view.getContext());

    }
    public void createNotification(String aMessage, Context context){

        final int NOTIFY_ID = 0; // ID of notification
        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = context.getString(R.string.default_notification_channel_title); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }
}
