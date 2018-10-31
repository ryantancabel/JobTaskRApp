package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClientHomePage extends Fragment {

    View view;
    ArrayList<TaskDataModel> activeList = new ArrayList<>();
    ArrayList<TaskDataModel> inactiveList = new ArrayList<>();
    RecyclerView ActiveRecyclerView, InactiveRecyclerView;
    private static Bundle b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null){

            view = inflater.inflate(R.layout.fragment_client_activity, container, false);
            Button addButton = (Button) view.findViewById(R.id.addButton);

            ActiveRecyclerView = (RecyclerView) view.findViewById(R.id.activeList);
            ActiveAdapter aAdapter = new ActiveAdapter(activeList);
            ActiveRecyclerView.setHasFixedSize(false);
            ActiveRecyclerView.setNestedScrollingEnabled(false);
            LinearLayoutManager ALayoutManager = new LinearLayoutManager(getActivity());
            ALayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ActiveRecyclerView.setLayoutManager(ALayoutManager);
            ActiveRecyclerView.setAdapter(aAdapter);
            intialiseActiveList();

            InactiveRecyclerView = (RecyclerView) view.findViewById(R.id.deletedList);
            InactiveAdapter iAdapter = new InactiveAdapter(inactiveList);
            InactiveRecyclerView.setHasFixedSize(false);
            InactiveRecyclerView.setNestedScrollingEnabled(false);
            LinearLayoutManager ILayoutManager = new LinearLayoutManager(getActivity());
            ILayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            InactiveRecyclerView.setLayoutManager(ILayoutManager);
            InactiveRecyclerView.setAdapter(iAdapter);
            intialiseInactiveList();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void intialiseActiveList() {

        ParseUser newUser = new ParseUser();
        String id = newUser.getObjectId();

        //parse query connect to your table
        ParseQuery<ParseObject> query =  ParseQuery.getQuery("Task");

        query.include("Task.User");

        query.whereEqualTo("Active", true);
        query.whereEqualTo("UserPointer", id);
        //Sort by created at or can be used updated At column in the parse table
        query.orderByAscending("_created_at");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null)
                {

                    //dataModelList.clear();
                    for(ParseObject task : taskList)
                    {
                        Location androidAddress = new Location("dummyprovider");

                        if (task.getParseGeoPoint("Location") != null) {
                            ParseGeoPoint parseAddress = task.getParseGeoPoint("Location");

                            double latitude = parseAddress.getLatitude();
                            double longitude = parseAddress.getLongitude();
                            androidAddress.setLatitude(latitude);
                            androidAddress.setLongitude(longitude);
                        } else {
                            androidAddress.setLatitude(0);
                            androidAddress.setLongitude(0);
                        }

                        String userName = null;
                        String email = null;

                        ParseObject user = task.getParseObject("UserPointer");
                        try {
                            userName = user.fetchIfNeeded().getString("username");
                            email = user.fetchIfNeeded().getString("email");
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        TaskDataModel mTaskData = new TaskDataModel(task.getString("Title"), task.getString("Description"),
                                task.getParseFile("Image"), androidAddress, task.getDate("TaskWhen"),
                                task.getDate("PostedWhen"), task.getDouble("TaskRate"), userName,
                                email);

                        activeList.add(mTaskData);
                    }
                }
                else
                {
                    Log.d(getClass().getSimpleName(),"Error:" + e.getMessage());
                }
                saveActiveList(activeList);
            }
        });
    }

    public void saveActiveList(ArrayList<TaskDataModel> list){

        activeList = list;

        if (activeList.size() > 0 & ActiveRecyclerView != null) {
            ActiveRecyclerView.setAdapter(new ClientHomePage.ActiveAdapter(activeList));
        }
    }

    private void intialiseInactiveList() {

        ParseUser newUser = new ParseUser();
        String id = newUser.getObjectId();

        //parse query connect to your table
        ParseQuery<ParseObject> query =  ParseQuery.getQuery("Task");

        query.include("Task.User");

        query.whereEqualTo("Active", false);
        query.whereEqualTo("UserPointer", id);
        //Sort by created at or can be used updated At column in the parse table
        query.orderByAscending("_created_at");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null)
                {

                    //dataModelList.clear();
                    for(ParseObject task : taskList)
                    {
                        Location androidAddress = new Location("dummyprovider");

                        if (task.getParseGeoPoint("Location") != null) {
                            ParseGeoPoint parseAddress = task.getParseGeoPoint("Location");

                            double latitude = parseAddress.getLatitude();
                            double longitude = parseAddress.getLongitude();
                            androidAddress.setLatitude(latitude);
                            androidAddress.setLongitude(longitude);
                        } else {
                            androidAddress.setLatitude(0);
                            androidAddress.setLongitude(0);
                        }

                        String userName = null;
                        String email = null;

                        ParseObject user = task.getParseObject("UserPointer");
                        try {
                            userName = user.fetchIfNeeded().getString("username");
                            email = user.fetchIfNeeded().getString("email");
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        TaskDataModel mTaskData = new TaskDataModel(task.getString("Title"), task.getString("Description"),
                                task.getParseFile("Image"), androidAddress, task.getDate("TaskWhen"),
                                task.getDate("PostedWhen"), task.getDouble("TaskRate"), userName,
                                email);

                        inactiveList.add(mTaskData);
                    }
                }
                else
                {
                    Log.d(getClass().getSimpleName(),"Error:" + e.getMessage());
                }
                saveActiveList(inactiveList);
            }
        });
    }

    public void saveInactiveList(ArrayList<TaskDataModel> list){

        inactiveList = list;

        if (inactiveList.size() > 0 & InactiveRecyclerView != null) {
            InactiveRecyclerView.setAdapter(new ClientHomePage.InactiveAdapter(inactiveList));
        }
    }

    public class ActiveAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<TaskDataModel> activeList;

        public ActiveAdapter(ArrayList<TaskDataModel> Data) {
            activeList = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_card, parent, false);
            ClientHomePage.MyViewHolder holder = new ClientHomePage.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ClientHomePage.MyViewHolder holder, int position) {

            //add Task Name
            holder.titleTextView.setText(activeList.get(position).getTaskName());

            //add Task Price
            String y = "$" + new DecimalFormat("#").format(activeList.get(position).getTaskRate());
            holder.price.setText(y);

            //add Task Picture
            ParseFile x = activeList.get(position).getPicture();
            if (x != null) {
                String imageUrl = x.getUrl();
                Picasso
                        .get()
                        .load(imageUrl)
                        .resize(100, 100)
                        .into(holder.itemPhoto);
            }

            //add Task Location
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            Location locality = activeList.get(position).getAddress();
            double latLocality = locality.getLatitude();
            double lonLocality = locality.getLongitude();
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(latLocality, lonLocality, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String city = addresses.get(0).getLocality();
            holder.location.setText(city);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, String title) {

                    android.support.v4.app.FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    TaskDetailFragment tdf = new TaskDetailFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable("taskObject", activeList.get(position));
                    tdf.setArguments(arguments);

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                        ft.replace(R.id.taskListPlaceholder, tdf, getTag()).addToBackStack(getTag()).commit();

                    } else {

                        ft.replace(R.id.taskDetailPlaceholder, tdf, getTag()).addToBackStack(getTag()).commit();

                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return activeList.size();
        }
    }

    public class InactiveAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<TaskDataModel> inactiveList;

        public InactiveAdapter(ArrayList<TaskDataModel> Data) {
            inactiveList = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_card, parent, false);
            ClientHomePage.MyViewHolder holder = new ClientHomePage.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ClientHomePage.MyViewHolder holder, int position) {

            //add Task Name
            holder.titleTextView.setText(inactiveList.get(position).getTaskName());

            //add Task Price
            String y = "$" + new DecimalFormat("#").format(inactiveList.get(position).getTaskRate());
            holder.price.setText(y);

            //add Task Picture
            ParseFile x = inactiveList.get(position).getPicture();
            if (x != null) {
                String imageUrl = x.getUrl();
                Picasso
                        .get()
                        .load(imageUrl)
                        .resize(100, 100)
                        .into(holder.itemPhoto);
            }

            //add Task Location
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            Location locality = inactiveList.get(position).getAddress();
            double latLocality = locality.getLatitude();
            double lonLocality = locality.getLongitude();
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(latLocality, lonLocality, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String city = addresses.get(0).getLocality();
            holder.location.setText(city);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, String title) {

                    android.support.v4.app.FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    TaskDetailFragment tdf = new TaskDetailFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable("taskObject", inactiveList.get(position));
                    tdf.setArguments(arguments);

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                        ft.replace(R.id.taskListPlaceholder, tdf, getTag()).addToBackStack(getTag()).commit();

                    } else {

                        ft.replace(R.id.taskDetailPlaceholder, tdf, getTag()).addToBackStack(getTag()).commit();

                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return inactiveList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView;
        public ImageView itemPhoto;
        public TextView price;
        public TextView location;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View v) {
            super(v);
            itemPhoto = (ImageView) v.findViewById(R.id.itemPhoto);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            price = (TextView) v.findViewById(R.id.price);
            location = (TextView) v.findViewById(R.id.location);

            v.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v, getAdapterPosition(), titleTextView.getText().toString());

        }
    }



}
