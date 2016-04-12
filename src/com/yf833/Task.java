package com.yf833;

import java.util.concurrent.LinkedBlockingQueue;


// represents a task (process)
public class Task {

    public int taskID;                  // the task's ID
    public int total_time;              // the total time (# of cycles) that the task has been in the system for
    public int waiting_time;            // the amount of time (# of cycles) that the task has been in the blocked queue

    public int[] initial_claims;        // an array to hold the initial resource claims (for all resource types)

    public boolean isBlocked = false;   // tracks if this task is blocked or not
    public boolean isAborted = false;   // set to true if the task is aborted (used when printing output)

    public LinkedBlockingQueue<Activity> activities;    // a queue of activites for this task


    // constructor //
    public Task(int taskID, int num_resources){
        this.taskID = taskID;

        this.activities = new LinkedBlockingQueue<>();

        //default values for total_time and waiting_time
        this.total_time = 0;
        this.waiting_time = 0;

        this.initial_claims = new int[num_resources];
    }


    // copy constructor
    public Task(Task t2){

        this.taskID = t2.taskID;

        //default values for total_time and waiting_time
        this.total_time = t2.total_time;
        this.waiting_time = t2.waiting_time;

        this.initial_claims = t2.initial_claims.clone();

        // copy activities queue
        this.activities = new LinkedBlockingQueue<>();
        for(Activity a : t2.activities){
            this.activities.add(new Activity(a));
        }

    }


    public String toString(){

        String output = "";

        output += "\n\n------------------------------\nTask " + this.taskID + "\n------------------------------\n";
        output += "total_time: " + this.total_time + "\n";
        output += "waiting_time: " + this.waiting_time + "\n";
        output += "max additional request (for resource 1): " + getMaxAdditionalRequest(1, this.initial_claims[0]) + "\n";

//        output += "next activity: " + this.activities.peek();
        output += "activities: " + this.activities.toString();

        return output;
    }


    // takes a resourceID, returns the maximum additional request for that resource
    public int getMaxAdditionalRequest(int resource_id, int current_allocation){

        int max_additional_request = this.initial_claims[resource_id-1] - current_allocation;

        return max_additional_request;
    }


}










