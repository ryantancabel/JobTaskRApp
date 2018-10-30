package ict376.murdoch.edu.au.jobtaskrapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class TaskDetailFragment extends Fragment {

    TaskDataModel task;
    //for notification
    private NotificationManager notifManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.task_detail, container, false);

        ImageView taskImage = (ImageView) view.findViewById(R.id.task_image);
        Button emailButton = (Button) view.findViewById(R.id.emailClient);
        Button emailButton2 = (Button) view.findViewById(R.id.emailClient2);
        ImageView profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView description = (TextView) view.findViewById(R.id.description);
        Button notification = view.findViewById(R.id.bt_notification);

        Bundle b = getArguments();
        task = (TaskDataModel) b.getSerializable("taskObject");

        //loading content

        ParseFile x = task.getPicture();
        if(x != null) {
            String imageUrl = x.getUrl();
            Picasso
                    .get()
                    .load(imageUrl)
                    .resize(250,250)
                    .into(taskImage);
        }

        titleTextView.setText(task.getTaskName());

        price.setText(task.getTaskRate().toString());

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        Location locality = task.getAddress();
        double latLocality = locality.getLatitude();
        double lonLocality = locality.getLongitude();
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latLocality, lonLocality, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city = addresses.get(0).getLocality();
        location.setText(city);

        description.setText(task.getTaskDescp());

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification("Task accepted");

            }
        });

        return view;
    }
    private void createNotification(String aMessage)
    {
        final int NOTIFY_ID = 0; // ID of notification
        String id = getActivity().getString(R.string.default_notification_channel_id); // default_channel_id
        String title = getActivity().getString(R.string.default_notification_channel_title); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(getActivity(), id);
            intent = new Intent(getActivity(), CreateTaskActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(getActivity().getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)         // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(getActivity(), id);
            intent = new Intent(getActivity(), CreateTaskActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(getActivity().getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);

    }


    public void createEmail() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, task.getEmailAddress());
        intent.putExtra(Intent.EXTRA_SUBJECT, "I'm here to help!");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Select your preferred email app"));

    }

    public void getDirections() {
        Uri directionUri = Uri.parse("google.streetview:cbll=" + task.getAddress().getLatitude() +
        "," + task.getAddress().getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, directionUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);
    }

}
