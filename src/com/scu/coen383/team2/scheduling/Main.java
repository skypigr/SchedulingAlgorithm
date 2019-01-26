package com.scu.coen383.team2.scheduling;

import java.util.PriorityQueue;

public class Main {
    private static final int COUNT_ALGORITHM = 8;

    public static void main(String[] args) {


        // loading Scheduling algorithms here
        FirstComeFirstServed FCFS = new FirstComeFirstServed();
        NonpreemptiveHighestPriorityFirst NP_HPF = new NonpreemptiveHighestPriorityFirst();

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


        System.out.println("\nNonpreemptive Highest Priority First (No Aging)");
        NP_HPF.schedule(priorityQueues[5]);

    }
}
