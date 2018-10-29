package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

        return view;
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
