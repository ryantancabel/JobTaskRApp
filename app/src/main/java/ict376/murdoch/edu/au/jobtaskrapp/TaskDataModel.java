package ict376.murdoch.edu.au.jobtaskrapp;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseFile;

import java.io.Serializable;
import java.util.Date;

public class TaskDataModel implements Serializable, Parcelable {

    private String taskName;
    private String taskDesc;
    private transient ParseFile picture;
    private transient Location address;
    private Date dateWhen;
    private Date datePosted;
    private Double taskRate;
    private String clientName;
    private String emailAddress;
    private String objectID;


    //add default constructor:
    public TaskDataModel(){}

    public TaskDataModel(String taskName, String taskDescp, ParseFile picture, Location address,
                         Date dateWhen, Date datePosted, Double taskRate, String clientName,
                         String emailAddress, String objectID)
    {
        super();
        setTaskName(taskName);
        setTaskDescp(taskDescp);
        setPicture(picture);
        setAddress(address);
        setDateWhen(dateWhen);
        setDatePosted(datePosted);
        setTaskRate(taskRate);
        setClientName(clientName);
        setEmailAddress(emailAddress);
        setObjectID(objectID);

    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescp(String taskDescp) {
        this.taskDesc = taskDescp;
    }

    public void setPicture(ParseFile picture) {
        this.picture = picture;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    public void setDateWhen(Date dateWhen) {
        this.dateWhen = dateWhen;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setTaskRate(Double taskRate) {
        this.taskRate = taskRate;
    }

    public void setClientName(String clientName) {this.clientName = clientName; }

    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress; }

    public void setObjectID(String objectID) {this.objectID = objectID; }

    public String getTaskDescp() {
        return taskDesc;
    }

    public String getTaskName() {
        return taskName;
    }

    public ParseFile getPicture() { return picture; }

    public Location getAddress() { return address; }

    public Date getDateWhen() { return dateWhen; }

    public Date getDatePosted() { return datePosted; }

    public Double getTaskRate() {return taskRate; }

    public String getClientName() {return clientName; }

    public String getEmailAddress() {return emailAddress; }

    public String getObjectID() {return objectID; }

    @Override
    public String toString()
    {
        return this.getTaskName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(taskName);
        dest.writeString(taskDesc);
        dest.writeParcelable(picture, 0);
        dest.writeParcelable(address, 0);
        dest.writeString(datePosted.toString());
        dest.writeString(dateWhen.toString());
        dest.writeDouble(taskRate);
        dest.writeString(clientName);
        dest.writeString(emailAddress);
        dest.writeString(objectID);

    }

    public static final Parcelable.Creator<TaskDataModel> CREATOR = new Parcelable.Creator<TaskDataModel>() {
        public TaskDataModel createFromParcel(Parcel in) {
            return new TaskDataModel();
        }

        public TaskDataModel[] newArray(int size) {
            return new TaskDataModel[size];
        }
    };
}
