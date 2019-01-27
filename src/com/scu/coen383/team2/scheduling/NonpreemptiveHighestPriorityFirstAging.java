package com.scu.coen383.team2.scheduling;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/*
    Ref: https://www.geeksforgeeks.org/program-for-preemptive-priority-cpu-scheduling/
         https://www.cs.rutgers.edu/~pxk/416/notes/07-scheduling.html

    After a process has waited for 5 quanta at a priority level, bump it up to next higher level.
    Process with same priority are scheduled by FCFS
 */
public class NonpreemptiveHighestPriorityFirstAging extends ScheduleBase {
    public Queue<Process> schedule(PriorityQueue<Process> q) {
        Queue<Process> scheduledQueue = new LinkedList<>();

        return scheduledQueue;
    }
}
