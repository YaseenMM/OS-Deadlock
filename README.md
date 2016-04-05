                
Implement FIFO for:
        > input-01 ***
        > input-02 ***
        > input-03 ***
        > input-04 ***
        > input-05 ***
        > input-06 ***
        > input-07 ***
        > input-08 ***
        > input-09 ***
        > input-10 ***
        > input-11 ***
        > input-12 ***
        > input-13 *** 
        
---------------------------------------------------------

(1) if a task's initial claim > resources present then abort ***

(*) if a task's requests > its claims then abort 

(*) implement a safe state check 

(*) call safe state check function before running any activities 

Implement Banker's for:
        > input-01 
        > input-02 
        > input-03 
        > input-04 
        > input-05 
        > input-06 
        > input-07 
        > input-08 
        > input-09 
        > input-10 
        > input-11 
        > input-12 
        > input-13 









(*) account for malformed input (multiple activities on one line; ignore whitespace) 

(*) set arbitrary limits for T and R values 

(*) put the required comments in your code 

---------------------------------------------------------------

>> if there are R resource types there are R initiate requests? 

>> The manager can process one activity (initiate, request, or release) for each task in one cycle.
   However, the terminate activity does NO T require a cycle.
   
>> The delay value represents the number of cycles between the completion of the previous activity for this
   process and the beginning of the current activity. 
   