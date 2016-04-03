package com.yf833;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    private static int num_tasks;

    private static int num_resource_types;
    private static ArrayList<Integer> resource_amounts = new ArrayList<>();

    private static LinkedBlockingQueue<Task> tasks = new LinkedBlockingQueue<>();




    ///// MAIN /////
    public static void main(String[] args) throws FileNotFoundException {


        // (1) get input from file; initialize variables  //

        if(args.length == 1){
            File inputfile = new File(args[0]);
            getInputFromFile(inputfile);
        }else{
            throw new IllegalArgumentException("Incorrect number of arguments; must provide filename of input");
        }

        printDebuggingInfo();





    }


    private static void printDebuggingInfo(){
        System.out.println("\nnum_tasks: " + num_tasks);
        System.out.println("\nnum_resource_types: " + num_resource_types);
        System.out.println("\nresource_amounts: " + resource_amounts.toString());
        System.out.println("\ntasks: " + tasks.toString());
    }


    private static void getInputFromFile(File inputfile) throws FileNotFoundException {
        Scanner input = new Scanner(inputfile);

        // initialize first line of input //
        num_tasks = input.nextInt();
        num_resource_types = input.nextInt();
        while(input.hasNextInt()){
            resource_amounts.add(input.nextInt());
        }

        // store activities into an arraylist and sort //
        ArrayList<Activity> activities = new ArrayList<>();
        while(input.hasNext()){
            Activity a = new Activity(input.next(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt());
            activities.add(a);
        }
        activities = Util.sortByTaskID(activities);

        // add activities to tasks //

        Task t = new Task(activities.get(0).taskID);
        int current = t.taskID;
        int i=0;
        for(Activity a : activities){

            if(a.taskID != current){
                tasks.add(t);               // add previous task
                current = a.taskID;         // increment current task
                t = new Task(a.taskID);     // create a new task
                t.activities.add(a);
            }else{
                t.activities.add(a);
            }

            //add the last task in the list
            if(current == num_tasks && i==activities.size()-1){
                tasks.add(t);
            }

            i++;
        }



    }





}
