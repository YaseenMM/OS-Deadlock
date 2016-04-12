package com.yf833;


// The activity class represents a task's activity
public class Activity {

    public String type;         // task type (request, claim, release, initiate, terminate)
    public int taskID;          // task ID for this activity
    public int delay;           // number of cycles to delay the activity by
    public int resourceID;      // the resource that this activity is for
    public int amount;          // claim amount, request amount, or release amount (depending on activity type)


    // constructor //
    public Activity(String type, int task_id){
        this.type = type;
        this.taskID = task_id;

        //default values for delay, resourceID, and amount
        this.delay = 0;
        this.resourceID = 0;
        this.amount = 0;
    }

    // constructor //
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
