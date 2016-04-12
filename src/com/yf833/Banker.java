package com.yf833;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;




public class Banker {

    public static int cycle = 0;
    public static boolean isDeadlocked = false;
    public static int[][] resource_claims;
    public static ArrayList<Task> finished_tasks = new ArrayList<>();

    public static ArrayList<Integer> available;
    public static ArrayList<Integer> freed;
    public static LinkedBlockingQueue<Task> blocked = new LinkedBlockingQueue<>();



    public static void runBanker(LinkedBlockingQueue<Task> tasks, ArrayList<Integer> resource_amounts){

        available = resource_amounts;
        freed = new ArrayList<>(Collections.nCopies(available.size(), 0));

        //initialize resource_claims[][]
        resource_claims = new int[tasks.size()][resource_amounts.size()];
        for(Task t : tasks){
            for(int j=0; j<resource_amounts.size(); j++){
                resource_claims[t.taskID-1][j] = 0;
            }
        }




        ///// Main Loop /////
        while(!tasks.isEmpty() || !blocked.isEmpty()){

            System.out.println("=============== CYCLE " + (cycle) + " - " + (cycle+1) + " ===============");



            ///// (1) check if blocked tasks can be serviced /////
            for(Task t : blocked){

                Activity current = t.activities.peek();


                //create copies of data strcutures for simulating within isSafe()
                // Task task, LinkedBlockingQueue<Task> tasks, ArrayList<Integer> available, int[][] claims

                Task task_copy = new Task(t);
                LinkedBlockingQueue<Task> tasks_copy = Util.copyTaskQueues(tasks, blocked);
                ArrayList<Integer> available_copy = new ArrayList<>(available);
                int[][] claims_copy = Util.copy2DArray(resource_claims);

                boolean is_safe = isSafe(task_copy, tasks_copy, available_copy, claims_copy);

                System.out.println("task" + t.taskID + " isSafe: " + is_safe);
                System.out.println("current.amount <= available.get(current.resourceID-1): " + (current.amount <= available.get(current.resourceID-1)));

                /////


                // try to claim the resource amount
                if(current.amount <= available.get(current.resourceID-1) && is_safe){
                    resource_claims[t.taskID-1][current.resourceID-1] += current.amount;
                    available.set(current.resourceID - 1, available.get(current.resourceID - 1) - current.amount);
                    t.activities.poll();
                    t.isBlocked = false;
                }else{
                    t.waiting_time++;
                }
            }


            ///// (2) for each task in the ready queue, try to run the next activity (if possible) /////
            for(Task t : tasks){

                Activity current = t.activities.peek();

                if(current.delay == 0){

                    if(current.type.equals("initiate")){

                        if(current.amount > available.get(current.resourceID-1))    {
                            tasks = abortUnsafeTask(tasks, t.taskID);
                        }
                        else{

                            t.initial_claims[current.resourceID-1] = current.amount;

                            t.activities.poll();
                        }



                    }
                    else if(current.type.equals("request")){

                        //create copies of data strcutures for simulating within isSafe()
                        // Task task, LinkedBlockingQueue<Task> tasks, ArrayList<Integer> available, int[][] claims

                        Task task_copy = new Task(t);
//                        LinkedBlockingQueue<Task> tasks_copy = new LinkedBlockingQueue<>(tasks);
                        LinkedBlockingQueue<Task> tasks_copy = Util.copyTaskQueues(tasks, blocked);
                        ArrayList<Integer> available_copy = new ArrayList<>(available);
                        int[][] claims_copy = Util.copy2DArray(resource_claims);


                        boolean is_safe = isSafe(task_copy, tasks_copy, available_copy, claims_copy);

                        System.out.println("task" + t.taskID + " isSafe: " + is_safe);
                        System.out.println("current.amount <= available.get(current.resourceID-1): " + (current.amount <= available.get(current.resourceID-1)));
                        System.out.println("!t.isBlocked: " + !t.isBlocked + "\n");

                        /////



                        if(current.amount + resource_claims[t.taskID-1][current.resourceID-1] > t.initial_claims[current.resourceID-1]){

                            //abort and return resources
                            tasks = abortUnsafeTask(tasks, t.taskID);

                        }
                        // try to claim the resource amount
                        else if(current.amount <= available.get(current.resourceID-1) && !t.isBlocked && is_safe){

                            System.out.println(">> request granted\n");

                            resource_claims[t.taskID-1][current.resourceID-1] += current.amount;
                            available.set(current.resourceID - 1, available.get(current.resourceID - 1) - current.amount);
                            t.activities.poll();

                        }else{

                            System.out.println(">> request not granted; task blocked\n");

                            t.waiting_time++;
                            t.isBlocked = true;

                            blocked.add(t);
                            tasks.remove(t);
                        }

                    }
                    else if(current.type.equals("release")){
                        resource_claims[t.taskID-1][current.resourceID-1] -= current.amount;
                        freed.set(current.resourceID-1, freed.get(current.resourceID-1) + current.amount);
                        t.activities.poll();

                    }

                    else if (current.type.equals("terminate")){
                        t.total_time = cycle;
                        finished_tasks.add(t);
                        tasks.remove(t);

                        t.activities.poll();
                    }

                }
                else{
                    current.delay--;
                }



            }

            ///// (3) add all unblocked tasks back to ready queue /////
            for(Task t : blocked){
                if(t.isBlocked == false){
                    tasks.add(t);
                    blocked.remove(t);
                }
            }


            ///// (4) move freed resources to available /////
            for(int i=0; i<available.size(); i++){
                available.set(i, available.get(i) + freed.get(i));
                freed.set(i, 0);
            }


            cycle++;




            ///// DEBUGGING INFO /////

            System.out.println("\nready:");
            System.out.println(tasks.toString());

            System.out.println("\nblocked:");
            System.out.println(blocked.toString());

            System.out.println("\nclaims:");
            Util.print2DArray(resource_claims);

            System.out.println("\navailable:");
            System.out.println(available.toString() + "\n");

        }





        // print output //
        System.out.println("\nBANKER\n");
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
        System.out.println(((float) wait_sum / (float) time_sum));


    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////



    // simulate what would happen if the next request is granted
    public static boolean isSafe(Task task, LinkedBlockingQueue<Task> tasks, ArrayList<Integer> available, int[][] claims){

        // if there are no processes remaining, the state is safe
        if(tasks.size() == 0){
            return true;
        }

        Activity request = task.activities.peek();  // get the request to simulate

        // if a task's requests <= available resources; simulate that task
        if(exceedsAvailableResources(task, available, claims) == false){

            ///// simulate for the current task /////

            // grant request for current
            available.set(request.resourceID-1, available.get(request.resourceID-1) - request.amount);
            claims[task.taskID-1][request.resourceID-1] += request.amount;

            //poll the current request
            for(Task t : tasks){
                if(t.taskID == task.taskID){
                    t.activities.poll();
                }
            }


            // SIMULATE: keep granting remaining request amounts for all tasks in the set


            // for each resource in available
            for(int i=0; i<available.size(); i++){


                while(allocationIsPossible(i+1, available.get(i), tasks, claims) && !tasks.isEmpty()){

                    // for each task in tasks
                    for(Task t : tasks){

                        // if the available units for a resource are enough to satisfy the maxadditionalrequests:
                        // add t's claims (current allocation) for resource i back to available
                        // subtract released claims (current allocation) from claims array

                        int max_additional_request = t.getMaxAdditionalRequest(i+1, claims[t.taskID-1][i]);

                        System.out.println("max_additional_request: " + max_additional_request);

                        if(available.get(i) >= max_additional_request){

                            available.set(i, available.get(i) + claims[t.taskID - 1][i]);
                            claims[t.taskID-1][i] = 0;

                            tasks.remove(t);
                        }


                    }


                }


            }


            // if not all claims are able to be granted, return false
            // (check if there are any remaining claims in the matrix >0)
            for(int i=0; i<claims.length; i++){
                for(int j=0; j<claims[0].length; j++){
                    if(claims[i][j] != 0){

//                        System.out.println("---> CLAIMS ARRAY IS NOT EMPTY");
//                        Util.print2DArray(claims);

                        return false;
                    }
                }
            }
            //if every task is able to terminate, return true
            return true;


        }
        // if a task's request > available resources; return UNSAFE
        else{

//            System.out.println("---> REQUEST > AVAILABLE");
            return false;
        }


    }


    // check if ANY of the tasks have a max additional claim <= available
    public static boolean allocationIsPossible(int resourceID, int available_amount, LinkedBlockingQueue<Task> tasks, int[][] claims){

        for(Task t : tasks){
            if(t.getMaxAdditionalRequest(resourceID, claims[t.taskID-1][resourceID-1]) <= available_amount){

                System.out.println("max additional request: " + t.getMaxAdditionalRequest(resourceID, claims[t.taskID-1][resourceID-1]));
                System.out.println("available_amount: " + available_amount);

                return true;
            }
        }

        return false;
    }



    // returns false if all of a given task's additional requests are < available units (for all resource types)
    public static boolean exceedsAvailableResources(Task t, ArrayList<Integer> available, int[][] claims){

        // for each resource in available
        // check t's max additional request for each resource type < available for that resource
        for(int i=0; i<available.size(); i++){
            if(t.getMaxAdditionalRequest(i+1, claims[t.taskID-1][i]) > available.get(i)){
                return true;
            }
        }

        return false;
    }







    // abort a task and release its resources
    public static LinkedBlockingQueue<Task> abortUnsafeTask(LinkedBlockingQueue<Task> ready_tasks, int task_id){

        for(Task t : ready_tasks){
            if(t.taskID == task_id){


                t.isAborted = true;
                finished_tasks.add(t);
                ready_tasks.remove(t);


                //release all of t's claims and add them back to available
                for(int j=0; j<resource_claims[t.taskID-1].length; j++){
                    int claim = resource_claims[t.taskID-1][j];
                    resource_claims[t.taskID-1][j] = 0;
                    available.set(j, available.get(j) + claim);
                }

                System.out.println("Task " + t.taskID + " aborted");
            }
        }

        return ready_tasks;
    }


}
