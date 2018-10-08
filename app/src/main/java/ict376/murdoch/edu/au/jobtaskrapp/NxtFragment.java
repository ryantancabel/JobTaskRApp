package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Intent;
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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class NxtFragment extends Fragment {

    ArrayList<TaskDataModel> dataModelList = new ArrayList<>();
    RecyclerView MyRecyclerView;

    private static final String TAG = NxtActivity.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Available Tasks");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nxt_layout, container, false);
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
                    .inflate(R.layout.nxt_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            holder.titleTextView.setText(list.get(position).getTaskName());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
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
                        TaskDataModel mTaskData = new TaskDataModel(task.getString("Title"), task.getString("Description"));

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
