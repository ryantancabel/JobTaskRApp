package ict376.murdoch.edu.au.jobtaskrapp;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment {

    ArrayList<TaskDataModel> dataModelList = new ArrayList<>();
    RecyclerView MyRecyclerView;

    private static final String TAG = TaskSearchActivity.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Available Tasks");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.task_search_list, container, false);
        Button addButton = (Button) view.findViewById(R.id.addButton);

        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        initializeList();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<TaskDataModel> list;

        public MyAdapter(ArrayList<TaskDataModel> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_card, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            //add Task Name
            holder.titleTextView.setText(list.get(position).getTaskName());

            //add Task Price
            String y = "$" + new DecimalFormat("#").format(list.get(position).getTaskRate());
            holder.price.setText(y);

            //add Task Picture
            ParseFile x = list.get(position).getPicture();
            if(x != null) {
                String imageUrl = x.getUrl();
                Picasso
                        .get()
                        .load(imageUrl)
                        .resize(100,100)
                        .into(holder.itemPhoto);
            }

            //add Task Location
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            Location locality = list.get(position).getAddress();
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

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView itemPhoto;
        public TextView price;
        public TextView location;

        public MyViewHolder(View v) {
            super(v);
            itemPhoto = (ImageView) v.findViewById(R.id.itemPhoto);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            price = (TextView) v.findViewById(R.id.price);
            location = (TextView) v.findViewById(R.id.location);

        }
    }

    public void initializeList() {

        //parse query connect to your table
        ParseQuery<ParseObject> query =  ParseQuery.getQuery("Task");

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

                        if(task.getParseGeoPoint("Location") != null) {
                            ParseGeoPoint parseAddress = task.getParseGeoPoint("Location");

                            double latitude = parseAddress.getLatitude();
                            double longitude = parseAddress.getLongitude();
                            androidAddress.setLatitude(latitude);
                            androidAddress.setLongitude(longitude);
                        }
                        else {
                            androidAddress.setLatitude(0);
                            androidAddress.setLongitude(0);
                        }


                        TaskDataModel mTaskData = new TaskDataModel(task.getString("Title"), task.getString("Description"),
                                task.getParseFile("Image"), androidAddress, task.getDate("TaskWhen"),
                                task.getDate("PostedWhen"), task.getDouble("TaskRate"));

                        dataModelList.add(mTaskData);
                    }
                }
                else
                {
                    Log.d(getClass().getSimpleName(),"Error:" + e.getMessage());
                }
                Log.d(TAG, "donex: " + dataModelList.get(0).getTaskDescp() + dataModelList.size());
                saveList(dataModelList);
            }
        });
    }

    public void saveList(ArrayList<TaskDataModel> list){

        dataModelList = list;

        if (dataModelList.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(dataModelList));
        }
    }

}