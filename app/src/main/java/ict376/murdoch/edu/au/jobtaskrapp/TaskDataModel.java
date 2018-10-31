package ict376.murdoch.edu.au.jobtaskrapp;


import android.location.Location;
import com.parse.ParseFile;

import java.io.Serializable;
import java.util.Date;

public class TaskDataModel implements Serializable {

    private String taskName;
    private String taskDesc;
    private transient ParseFile picture;
    private transient Location address;
    private Date dateWhen;
    private Date datePosted;
    private Double taskRate;
    private String clientName;
    private String emailAddress;


    //add default constructor:
    public TaskDataModel(){}

    public TaskDataModel(String taskName, String taskDescp, ParseFile picture, Location address,
                         Date dateWhen, Date datePosted, Double taskRate, String clientName,
                         String emailAddress)
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

    @Override
    public String toString()
    {
        return this.getTaskName();
    }

}
