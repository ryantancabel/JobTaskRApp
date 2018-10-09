package ict376.murdoch.edu.au.jobtaskrapp;


import android.os.Parcel;
import android.os.Parcelable;

public class TaskDataModel /*implements Parcelable*/ {

    public String taskName;
    public String taskDescp;

    public TaskDataModel(Parcel source)
    {
        super();
    }

    public TaskDataModel(String taskName, String taskDescp)
    {
        super();
        setTaskName(taskName);
        setTaskDescp(taskDescp);
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescp(String taskDescp) {
        this.taskDescp = taskDescp;
    }

    public String getTaskDescp() {
        return taskDescp;
    }

    public String getTaskName() {
        return taskName;
    }

    @Override
    public String toString()
    {
        return this.getTaskName();
    }

   /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(taskName);
        dest.writeString(taskDescp);

    }

    public class MyCreator implements Parcelable.Creator<TaskDataModel> {
        public TaskDataModel createFromParcel(Parcel source) {
            return new TaskDataModel(source);
        }
        public TaskDataModel[] newArray(int size) {
            return new TaskDataModel[size];
        }
    }

    public TaskDataModel(Parcel source){

        taskDescp = source.readString();
        taskName = source.readString();

    } */

    //Parcelable Methods


}
