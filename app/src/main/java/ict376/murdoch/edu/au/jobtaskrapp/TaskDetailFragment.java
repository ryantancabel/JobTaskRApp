package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class TaskDetailFragment extends Fragment {

    public TaskDataModel task;

    public static TaskDetailFragment newInstance()
    {
        TaskDetailFragment t = new TaskDetailFragment();
        return t;
    }

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
        Button navigate = (Button) view.findViewById(R.id.navigate);
        ImageView profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView description = (TextView) view.findViewById(R.id.description);

        Bundle b = getArguments();
        task = (TaskDataModel) b.getParcelable("taskObject");

        Log.d(TAG, "onCreateView: " + task.getEmailAddress());

        //loading content

        ParseFile x = task.getPicture();
        if(x != null) {
            String imageUrl = x.getUrl();
            Picasso
                    .get()
                    .load(imageUrl)
                    .fit()
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

        emailButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                createEmail();

            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections();
            }
        });

        return view;
    }

    public void createEmail() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {task.getEmailAddress()} );
        intent.putExtra(Intent.EXTRA_SUBJECT, "I'm here to help!");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi " + task.getClientName() + " !");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Select your preferred email app"));
        }

    }

    public void getDirections() {

        double lat = task.getAddress().getLatitude();
        double lon = task.getAddress().getLongitude();
        String head = task.getTaskName();

        String geoUriString="geo:"+lat+","+lon+"?q="+head+"@"+lat+","+lon;
        Uri geoUri = Uri.parse(geoUriString);
        Intent mapCall  = new Intent(Intent.ACTION_VIEW, geoUri);
        startActivity(mapCall);
    }




}
