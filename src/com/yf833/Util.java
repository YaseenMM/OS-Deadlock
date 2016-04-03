package com.yf833;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Util {

    public static ArrayList<Activity> sortByTaskID(ArrayList<Activity> activities){

        Collections.sort(activities, new Comparator<Activity>() {

            public int compare(Activity a1, Activity a2) {
                return a1.taskID < a2.taskID ? -1 : 1;
            }

        });

        return activities;
    }



}
