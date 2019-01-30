package com.scu.coen383.team2.scheduling;

import java.util.PriorityQueue;

public class Main {
    private static final int COUNT_ALGORITHM = 9;

    public static void main(String[] args) {


        // loading Scheduling algorithms here
        FirstComeFirstServed                    FCFS        = new FirstComeFirstServed();
        ShortestRemainingTime                   SRT         = new ShortestRemainingTime();
        NonpreemptiveHighestPriorityFirst       NP_HPF      = new NonpreemptiveHighestPriorityFirst();
        PreemptiveHighestPriorityFirst          P_HPF       = new PreemptiveHighestPriorityFirst();
        NonpreemptiveHighestPriorityFirstAging  NP_HPF_AG   = new NonpreemptiveHighestPriorityFirstAging();
        PreemptiveHighestPriorityFirstAging     P_HPF_AG    = new PreemptiveHighestPriorityFirstAging();

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

        System.out.println("\nShortest remaining time");
        SRT.schedule(priorityQueues[2]);


        System.out.println("\nNonpreemptive Highest Priority First");
        NP_HPF.schedule(priorityQueues[5]);
        System.out.println("\nNonpreemptive Highest Priority First (Aging)");
        NP_HPF_AG.schedule(priorityQueues[6]);

        System.out.println("\nPreemptive Highest Priority First");
        P_HPF.schedule(priorityQueues[7]);
        System.out.println("\nPreemptive Highest Priority First (Aging)");
        P_HPF_AG.schedule(priorityQueues[8]);



    }
}
