package com.yf833;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Util {

    public static ArrayList<Activity> sortActivitiesByID(ArrayList<Activity> activities){

        Collections.sort(activities, new Comparator<Activity>() {

            public int compare(Activity a1, Activity a2) {
                return a1.taskID < a2.taskID ? -1 : 1;
            }

        });

        return activities;
    }

    public static ArrayList<Task> sortTasksByID(ArrayList<Task> tasks){

        Collections.sort(tasks, new Comparator<Task>() {

            public int compare(Task t1, Task t2) {
                return t1.taskID < t2.taskID ? -1 : 1;
            }

        });

        return tasks;
    }



    public static void print2DArray(int [][] array){

        if(array == null || array.length < 1 || array[0].length < 1){
            throw new IllegalArgumentException("array is empty");
        }

        for (int i=0; i< array.length; i++){

            for(int j=0; j<array[0].length; j++){
                System.out.print("[" + array[i][j] + "]");
            }

            System.out.println();
        }

    }



    public static void printTaskSumamry(Task t){
        System.out.print("Task " + t.taskID + "\t");
        System.out.print(t.total_time + "\t");
        System.out.print(t.waiting_time + "\t");
        System.out.print(((float) t.waiting_time / (float) t.total_time) + "\t");
        System.out.println();
    }

}
