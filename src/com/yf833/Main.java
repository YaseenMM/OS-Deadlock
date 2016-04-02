package com.yf833;

import javax.activity.ActivityCompletedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static int num_tasks;

    private static int num_resource_types;
    private static ArrayList<Integer> resource_amounts = new ArrayList<>();

    private static ArrayList<Activity> activities = new ArrayList<Activity>();




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
        System.out.println("num_tasks: " + num_tasks);
        System.out.println("num_resource_types: " + num_resource_types);
        System.out.println("resource_amounts: " + resource_amounts.toString());
        System.out.println("activities: " + activities.toString());
    }


    private static void getInputFromFile(File inputfile) throws FileNotFoundException {
        Scanner input = new Scanner(inputfile);

        // initialize first line of input //
        num_tasks = input.nextInt();
        num_resource_types = input.nextInt();
        while(input.hasNextInt()){
            resource_amounts.add(input.nextInt());
        }

        // add activities //
        while(input.hasNext()){
            Activity a = new Activity(input.next(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt());
            activities.add(new Activity(input.next(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt()));
        }
    }





}
