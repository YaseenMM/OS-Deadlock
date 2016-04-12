package com.yf833;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;



public class Optimistic {


    public static int cycle = 0;                                                    // counter for current cycle #
    public static boolean isDeadlocked = false;                                     // tracks whether the system is in a state of deadlock or not
    public static int[][] resource_claims;                                          // a 2D array to lookup current resource claims for all tasks
    public static ArrayList<Task> finished_tasks = new ArrayList<>();               // contains tasks that have terminated or aborted (used when printing output)
    public static ArrayList<Integer> available;                                     // an arraylist of availble resource amounts for all resources
    public static ArrayList<Integer> freed;                                         // an arraylist for keeping track of how many units of each resource have been freed in this cycle
    public static LinkedBlockingQueue<Task> blocked = new LinkedBlockingQueue<>();  // a queue for blocked tasks


    // run the fifo simulation for the given resources and tasks //
    public static void runFifo(LinkedBlockingQueue<Task> tasks, ArrayList<Integer> resource_amounts){

        available = resource_amounts;
        freed = new ArrayList<>(Collections.nCopies(available.size(), 0));

        //initialize resource_claims[][] with 0s
        resource_claims = new int[tasks.size()][resource_amounts.size()];
        for(Task t : tasks){
            for(int j=0; j<resource_amounts.size(); j++){
                resource_claims[t.taskID-1][j] = 0;
            }
        }


        ///// Main Loop /////
        while(!tasks.isEmpty() || !blocked.isEmpty()){

            //check if blocked tasks can be serviced
            for(Task t : blocked){

                Activity current = t.activities.peek();

                // try to claim the resource amount
                if(current.amount <= available.get(current.resourceID-1)){
                    resource_claims[t.taskID-1][current.resourceID-1] += current.amount;
                    available.set(current.resourceID - 1, available.get(current.resourceID - 1) - current.amount);
                    t.activities.poll();
                    t.isBlocked = false;
                }else{
                    //increase waiting time if request is unable to be granted
                    t.waiting_time++;
                }
            }


            //for each task in the queue, try to run the next activity (if possible)
            for(Task t : tasks){

                Activity current = t.activities.peek();

                if(current.delay == 0){

                    if(current.type.equals("initiate")){
                        t.activities.poll();
                    }
                    else if(current.type.equals("request")){
                        // try to claim the resource amount, otherwise, add to blocked and increase waiting time
                        if(current.amount <= available.get(current.resourceID-1) && !t.isBlocked){
                            resource_claims[t.taskID-1][current.resourceID-1] += current.amount;
                            available.set(current.resourceID - 1, available.get(current.resourceID - 1) - current.amount);
                            t.activities.poll();

                        }else{
                            t.waiting_time++;
                            t.isBlocked = true;

                            blocked.add(t);
                            tasks.remove(t);
                        }

                    }
                    else if(current.type.equals("release")){
                        // release the task's claims into freed
                        resource_claims[t.taskID-1][current.resourceID-1] -= current.amount;
                        freed.set(current.resourceID-1, freed.get(current.resourceID-1) + current.amount);
                        t.activities.poll();

                    }

                    else if (current.type.equals("terminate")){
                        // terminate the task -- set the finish time, add it to finished, and remove it from the queue
                        t.total_time = cycle;
                        finished_tasks.add(t);
                        tasks.remove(t);

                        t.activities.poll();
                    }

                }
                else{
                    // if delay was not at 0 this cycle, decrement the delay counter
                    current.delay--;

                }


            }

            // add all unblocked tasks back to ready queue //
            for(Task t : blocked){
                if(t.isBlocked == false){
                    tasks.add(t);
                    blocked.remove(t);
                }
            }





            ///// detect deadlock /////
            // deadlock occurs when there items in blocked and nothing in the ready queue
            if(tasks.size() == 0 && blocked.size() != 0 && isDeadlocked == false){
                isDeadlocked = true;
            }
            ///// break deadlock if already deadlocked /////
            if(isDeadlocked){

                // while the next blocked task's activity can't be run; keep aborting tasks
                while(isAvailable() == false){

                    //abort the lowest numbered task
                    abortLowestTask();
                }

                //move next task to ready
                Task next = blocked.poll();
                next.isBlocked = false;
                tasks.add(next);

                isDeadlocked = false;

            }


            ///// move freed resources to available /////
            for(int i=0; i<available.size(); i++){
                available.set(i, available.get(i) + freed.get(i));
                freed.set(i, 0);
            }


            cycle++;


            ///// DEBUGGING INFO /////

//            System.out.println("\nready:");
//            System.out.println(tasks.toString());
//
//            System.out.println("\nblocked:");
//            System.out.println(blocked.toString());
//
//            System.out.println("\nclaims:");
//            Util.print2DArray(resource_claims);
//
//            System.out.println("\navailable:");
//            System.out.println(available.toString() + "\n");

        }


        // print output //
        System.out.println("\nFIFO");
        Util.sortTasksByID(finished_tasks);

        int time_sum = 0;
        int wait_sum = 0;

        for(Task t : finished_tasks){
            if(t.isAborted){
                System.out.println("Task " + t.taskID + "\taborted");
            }else{
                Util.printTaskSumamry(t);
                time_sum += t.total_time;
                wait_sum += t.waiting_time;
            }
        }
        System.out.print("total" + "\t" + time_sum + "\t" + wait_sum + "\t");

        int percent_total = Math.round(((float) wait_sum / (float) time_sum) * 100);
        System.out.println(percent_total + "%");

    }




    // check if the next task's activity in the blocked tasks can be granted
    public static boolean isAvailable(){
            Task next = blocked.peek();
            if(next.activities.peek().amount <= available.get(next.activities.peek().resourceID-1)){
                return true;
            }
        return false;
    }


    // abort the lowest numbered task in the blocked queue
    public static void abortLowestTask(){
        int lowestTaskID = blocked.peek().taskID;
        for(Task t : blocked){
            if(t.taskID < lowestTaskID){
                lowestTaskID = t.taskID;
            }
        }

        for(Task t : blocked){
            if(t.taskID == lowestTaskID){
                t.isAborted = true;
                finished_tasks.add(t);
                blocked.remove(t);

                //release all of t's claims and add them back to available
                for(int j=0; j<resource_claims[t.taskID-1].length; j++){
                    int claim = resource_claims[t.taskID-1][j];
                    resource_claims[t.taskID-1][j] = 0;
                    available.set(j, available.get(j) + claim);
                }

            }
        }

    }



}


