package ict376.murdoch.edu.au.jobtaskrapp;



public class TaskDataModel {

    public String taskName;
    public String taskDescp;

    public TaskDataModel()
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
}
