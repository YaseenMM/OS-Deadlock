
(1) get input from the user (name of input file) ***
(2) Create an Activity class ***
(3) Create a Task class ***


(4) write pseudocode for FIFO 
        > add waiting/blocked states (tasks can't get resources in the same cycle) 
        > must take care of blocked tasks before doing anything else 


(5) If deadlock is detected, print a message and abort the lowest numbered deadlocked task after releasing all its resources. If
    deadlock remains, print another message and abort the next lowest numbered deadlocked task, etc. (for FIFO)





(*) account for case where inputs for two tasks are interleaved (input 8)

(*) account for malformed input (multiple activities on one line; ignore whitespace) 

(*) set arbitrary limits for T and R values 

(*) put the required comments in your code 

---------------------------------------------------------------

>> if there are R resource types there are R initiate requests? 

>> The manager can process one activity (initiate, request, or release) for each task in one cycle.
   However, the terminate activity does NO T require a cycle.
   
>> The delay value represents the number of cycles between the completion of the previous activity for this
   process and the beginning of the current activity. 
   