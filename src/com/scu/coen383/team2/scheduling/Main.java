package com.scu.coen383.team2.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final int COUNT_ALGORITHM = 6;

    public static void main(String[] args) {


        // loading Scheduling algorithms here
        FirstComeFirstServed FCFS = new FirstComeFirstServed();


        PriorityQueue<Process>[] priorityQueues =  new PriorityQueue[COUNT_ALGORITHM + 1];

        // write your code here
        priorityQueues[0] = ProcessGenerator.generateJobs (1234, 20);

        // make a copy for each algorithm
        for (int i = 1; i < COUNT_ALGORITHM + 1; i++) {
            priorityQueues[i] = new PriorityQueue<Process>(priorityQueues[0]);
        }

        // print the process list in ascending order
        while (!priorityQueues[COUNT_ALGORITHM].isEmpty()) {
            System.out.println(priorityQueues[COUNT_ALGORITHM].poll());
        }


        // Add different scheduling algorithms here
        System.out.println("\nFisrt come first servered");
        FCFS.schedule(priorityQueues[0]);




    }
}
