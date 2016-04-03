package com.yf833;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class Task {

    public int taskID;
    public int total_time;
    public int waiting_time;
    public LinkedBlockingQueue<Activity> activities;


    public Task(int taskID){
        this.taskID = taskID;

        this.activities = new LinkedBlockingQueue<>();

        //default values for total_time and waiting_time
        this.total_time = 0;
        this.waiting_time = 0;
    }


    public String toString(){
        String output = "";

//        output += "------------------------------\n";

        output += "\n\n------------------------------\nTask " + this.taskID + "\n------------------------------\n";
        output += "total_time: " + this.total_time + "\n";
        output += "waiting_time: " + this.waiting_time + "\n";
        output += "activities: " + this.activities.toString();

//        output += "\n------------------------------\n";

        return output;
    }


}
