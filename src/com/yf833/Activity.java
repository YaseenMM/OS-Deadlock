package com.yf833;



public class Activity {

    public String type;
    public int taskID;
    public int delay;
    public int resourceID;
    public int claim;


    public Activity(String type, int task_id){
        this.type = type;
        this.taskID = task_id;

        //default values for delay, resourceID, and claim
        this.delay = 0;
        this.resourceID = 0;
        this.claim = 0;
    }

    public Activity(String type, int task_id, int delay, int resourceID, int claim){
        this.type = type;
        this.taskID = task_id;
        this.delay = delay;
        this.resourceID = resourceID;
        this.claim = claim;
    }


    public String toString(){
        return String.format("\n%-12s", this.type) + this.taskID + " " + this.delay + " " + this.resourceID + " " + this.claim;
    }

}
