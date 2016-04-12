package com.yf833;



public class Activity {

    public String type;
    public int taskID;
    public int delay;
    public int resourceID;
    public int amount;


    public Activity(String type, int task_id){
        this.type = type;
        this.taskID = task_id;

        //default values for delay, resourceID, and amount
        this.delay = 0;
        this.resourceID = 0;
        this.amount = 0;
    }

    public Activity(String type, int task_id, int delay, int resourceID, int amount){
        this.type = type;
        this.taskID = task_id;
        this.delay = delay;
        this.resourceID = resourceID;
        this.amount = amount;
    }

    // copy constructor //
    public Activity(Activity a2){
        this.type = a2.type;
        this.taskID = a2.taskID;
        this.delay = a2.delay;
        this.resourceID = a2.resourceID;
        this.amount = a2.amount;
    }




    public String toString(){
        return String.format("%-12s", this.type) + this.taskID + " " + this.delay + " " + this.resourceID + " " + this.amount;
    }

}
