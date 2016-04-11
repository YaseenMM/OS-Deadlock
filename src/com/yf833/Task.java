package com.yf833;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class Task {

    public int taskID;
    public int total_time;
    public int waiting_time;
    public boolean isBlocked = false;
    public boolean isAborted = false;


    public LinkedBlockingQueue<Activity> activities;


    public Task(int taskID){
        this.taskID = taskID;

        this.activities = new LinkedBlockingQueue<>();

        //default values for total_time and waiting_time
        this.total_time = 0;
        this.waiting_time = 0;
    }


    // copy constructor
    public Task(Task t2){

        this.taskID = t2.taskID;

        this.activities = new LinkedBlockingQueue<>(t2.activities);

        //default values for total_time and waiting_time
        this.total_time = t2.total_time;
        this.waiting_time = t2.waiting_time;


    }


    public String toString(){
        String output = "";

//        output += "------------------------------\n";

        output += "\n\n------------------------------\nTask " + this.taskID + "\n------------------------------\n";
        output += "total_time: " + this.total_time + "\n";
        output += "waiting_time: " + this.waiting_time + "\n";

        output += "max additional request (for resource 1): " + getMaxAdditionalRequest(1) + "\n";

        output += "next activity: " + this.activities.peek();

//        output += "\n------------------------------\n";

        return output;
    }


    // takes a resourceID, returns the maximum additional request for that resource
    public int getMaxAdditionalRequest(int resourceID){

        int max_additional_request = 0;

        for(Activity a : this.activities){
            if(a.type.equals("request")){
                max_additional_request += a.amount;
            }
        }

        return max_additional_request;
    }


}
