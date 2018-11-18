package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.internal.ILocationSourceDelegate;
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

public class ClientHomePage extends Fragment implements View.OnClickListener {

    View view;
    ArrayList<TaskDataModel> activeList = new ArrayList<>();
    ArrayList<TaskDataModel> inactiveList = new ArrayList<>();
    RecyclerView ActiveRecyclerView, InactiveRecyclerView;
    LinearLayoutManager ALayoutManager, ILayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String id = ParseUser.getCurrentUser().getString("objectId");

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private final String KEY_RECYCLER_STATE2 = "recycler_state";
    private static Bundle mBundle;
    private ArrayList<Parcelable> ActiveListState;
    private ArrayList<Parcelable> InactiveListState;

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
            addButton.setOnClickListener(this);

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setDistanceToTriggerSync(200);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Refresh items
                    mSwipeRefreshLayout.setRefreshing(true);
                    refreshItems();
                }
            });

            if(savedInstanceState != null)
            {
                activeList = savedInstanceState.getParcelableArrayList("act");
                inactiveList = savedInstanceState.getParcelableArrayList("inact");
                refreshingItems();
            }
            else {
                refreshItems();
            }

        }

        return view;
    }

    public void refreshItems()
    {
        activeList.clear();
        inactiveList.clear();

        intialiseActiveList();
        intialiseInactiveList();

        refreshingItems();
    }

    public void refreshingItems()
    {

        ActiveRecyclerView = (RecyclerView) view.findViewById(R.id.activeList);
        ActiveAdapter aAdapter = new ActiveAdapter(activeList);
        ActiveRecyclerView.setHasFixedSize(false);
        ActiveRecyclerView.setNestedScrollingEnabled(false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ALayoutManager = new LinearLayoutManager(getActivity());
            ALayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ActiveRecyclerView.setLayoutManager(ALayoutManager);
        }
        else {
            ALayoutManager = new GridLayoutManager(getActivity(), 2);
            ALayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            ActiveRecyclerView.setLayoutManager(ALayoutManager);
        }
        ActiveRecyclerView.setAdapter(aAdapter);


        InactiveRecyclerView = (RecyclerView) view.findViewById(R.id.deletedList);
        InactiveAdapter iAdapter = new InactiveAdapter(inactiveList);
        InactiveRecyclerView.setHasFixedSize(false);
        InactiveRecyclerView.setNestedScrollingEnabled(false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ILayoutManager = new LinearLayoutManager(getActivity());
            ILayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            InactiveRecyclerView.setLayoutManager(ILayoutManager);
        }
        else {
            ILayoutManager = new GridLayoutManager(getActivity(), 2);
            ILayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            InactiveRecyclerView.setLayoutManager(ILayoutManager);
        }
        InactiveRecyclerView.setAdapter(iAdapter);

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    private void intialiseActiveList() {

        ParseQuery<ParseObject> query =  ParseQuery.getQuery("Task");
        query.whereEqualTo("Active", true);
        query.whereEqualTo("UserPointer", ParseUser.getCurrentUser());


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
                                email, task.getObjectId());

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

        //parse query connect to your table
        ParseQuery<ParseObject> query =  ParseQuery.getQuery("Task");
        query.whereNotEqualTo("Active",true);
        query.whereEqualTo("UserPointer", ParseUser.getCurrentUser());
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
                                email, task.getObjectId());

                        inactiveList.add(mTaskData);
                    }
                }
                else
                {
                    Log.d(getClass().getSimpleName(),"Error:" + e.getMessage());
                }
                saveInactiveList(inactiveList);
            }
        });
    }

    public void saveInactiveList(ArrayList<TaskDataModel> list){

        inactiveList = list;

        if (inactiveList.size() > 0 & InactiveRecyclerView != null) {
            InactiveRecyclerView.setAdapter(new ClientHomePage.InactiveAdapter(inactiveList));
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), CreateTaskActivity.class);
        startActivity(intent);
    }

    public class ActiveAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<TaskDataModel> activeList;

        public ActiveAdapter(ArrayList<TaskDataModel> Data) {
            activeList = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_card_client_active, parent, false);
            ClientHomePage.MyViewHolder holder = new ClientHomePage.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ClientHomePage.MyViewHolder holder, final int position) {

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
                        .fit()
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


            holder.editButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditTaskDetail.class);
                    intent.putExtra("object", (Parcelable) activeList.get(position));
                    startActivity(intent);
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
                    .inflate(R.layout.task_card_client_active, parent, false);
            ClientHomePage.MyViewHolder holder = new ClientHomePage.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ClientHomePage.MyViewHolder holder, final int position) {

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
                        .fit()
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

            holder.editButton.setText("REACTIVATE");

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //update table by id
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
                    query.whereEqualTo("objectId", inactiveList.get(position).getObjectID());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                ParseObject task = list.get(0);
                                task.put("Active", true);
                                task.saveInBackground();
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });

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
        public Button editButton;

        public MyViewHolder(View v) {
            super(v);
            itemPhoto = (ImageView) v.findViewById(R.id.itemPhoto);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            price = (TextView) v.findViewById(R.id.price);
            location = (TextView) v.findViewById(R.id.location);
            editButton = (Button) v.findViewById(R.id.editButton);

        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundle = new Bundle();
        mBundle.putParcelableArrayList("act", activeList);
        mBundle.putParcelableArrayList("inact", inactiveList);
        super.onSaveInstanceState(mBundle);

    }

    @Override
    public void onResume()
    {
        super.onResume();

        // restore RecyclerView state
        if (mBundle != null) {
            activeList = mBundle.getParcelableArrayList("act");
            inactiveList = mBundle.getParcelableArrayList("inact");
            refreshingItems();

        }
    }



}
