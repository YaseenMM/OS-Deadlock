package com.yf833;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;




public class Util {


    // sort an arraylist of activities by ID in ascending order
    public static ArrayList<Activity> sortActivitiesByID(ArrayList<Activity> activities){
        Collections.sort(activities, new Comparator<Activity>() {

            public int compare(Activity a1, Activity a2) {
                return a1.taskID < a2.taskID ? -1 : 1;
            }

        });
        return activities;
    }


    // sort an arraylist of tasks by ID in ascending order
    public static ArrayList<Task> sortTasksByID(ArrayList<Task> tasks){
        Collections.sort(tasks, new Comparator<Task>() {
            public int compare(Task t1, Task t2) {
                return t1.taskID < t2.taskID ? -1 : 1;
            }
        });
        return tasks;
    }


    // helper function for printing a 2D array
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


    // helper function for creating a copy of a 2D array
    public static int[][] copy2DArray(int [][] array){
        int [][] copy = new int[array.length][];
        for(int i=0; i<array.length; i++) {
            copy[i] = array[i].clone();
        }
        return copy;
    }


    // create a copy of a tasks and blocking queues
    public static LinkedBlockingQueue<Task> copyTaskQueues(LinkedBlockingQueue<Task> tasks, LinkedBlockingQueue<Task> blocked){
        LinkedBlockingQueue<Task> newtasks = new LinkedBlockingQueue<>();
        for(Task t : blocked){
            newtasks.add(new Task(t));
        }
        for(Task t : tasks){
            newtasks.add(new Task(t));
        }
        return newtasks;
    }


    // create a copy of tasks queue
    public static LinkedBlockingQueue<Task> copyTaskQueue(LinkedBlockingQueue<Task> tasks){
        LinkedBlockingQueue<Task> newtasks = new LinkedBlockingQueue<>();
        for(Task t : tasks){
            newtasks.add(new Task(t));
        }
        return newtasks;
    }


    // prints a summary of a task (for final output)
    public static void printTaskSumamry(Task t){
        System.out.print("Task " + t.taskID + "\t");
        System.out.print(t.total_time + "\t");
        System.out.print(t.waiting_time + "\t");

        int percentval = Math.round(((float) t.waiting_time / (float) t.total_time) * 100);
        System.out.print(percentval + "%\t");
        System.out.println();
    }

}
