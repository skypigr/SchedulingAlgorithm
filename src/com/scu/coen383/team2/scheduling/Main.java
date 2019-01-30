package com.scu.coen383.team2.scheduling;

import java.util.PriorityQueue;

public class Main {
    private static final int COUNT_ALGORITHM = 9;
    private static final int ROUND  =   5;
    public static void main(String[] args) {


        // loading Scheduling algorithms here
        FirstComeFirstServed                    FCFS        = new FirstComeFirstServed();

        ShortestJobFirst                        SJF         = new ShortestJobFirst();
        ShortestRemainingTime                   SRT         = new ShortestRemainingTime();
        RoundRobin                              RR          = new RoundRobin();
        NonpreemptiveHighestPriorityFirst       NP_HPF      = new NonpreemptiveHighestPriorityFirst();
        PreemptiveHighestPriorityFirst          P_HPF       = new PreemptiveHighestPriorityFirst();
        NonpreemptiveHighestPriorityFirstAging  NP_HPF_AG   = new NonpreemptiveHighestPriorityFirstAging();
        PreemptiveHighestPriorityFirstAging     P_HPF_AG    = new PreemptiveHighestPriorityFirstAging();


        PriorityQueue<Process>[] priorityQueues =  new PriorityQueue[COUNT_ALGORITHM + 1];
        int [] SEEDS = new int[]{1234, 117777,1551,17111, 19191};

        for (int j = 0; j < ROUND; j++) {
            System.out.format("\n### Start Running Round %d ###\n", j);
            // write your code here
            priorityQueues[0] = ProcessGenerator.generateJobs (SEEDS[j], 20);

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

            System.out.println("\nShortest Job First");
            SJF.schedule(priorityQueues[1]);

            System.out.println("\nShortest remaining time");
            SRT.schedule(priorityQueues[2]);
            System.out.println("\nRoundRobin");
            RR.schedule(priorityQueues[4]);

            System.out.println("\nNonpreemptive Highest Priority First");
            NP_HPF.schedule(priorityQueues[5]);

            System.out.println("\nNonpreemptive Highest Priority First (Aging)");
            NP_HPF_AG.schedule(priorityQueues[6]);

            System.out.println("\nPreemptive Highest Priority First");
            P_HPF.schedule(priorityQueues[7]);

            System.out.println("\nPreemptive Highest Priority First (Aging)");
            P_HPF_AG.schedule(priorityQueues[8]);


        }


        System.out.println("\n");
        System.out.format("====================   5 Round Average Statistics  ====================\n");
        System.out.format("%10s %20s %20s %20s\n", "Algorithm", "Turnaround", "Waiting", "Responce");
        System.out.format("%10s %20.3f %20.3f %20.3f\n", "FCFS",
                FCFS.getStats().getAvgTurnaroundTime(),
                FCFS.getStats().getAvgWaitingTime(),
                FCFS.getStats().getAvgResponseTime());


        System.out.format("%10s %20.3f %20.3f %20.3f\n", "SJF",
                SJF.getStats().getAvgTurnaroundTime(),
                SJF.getStats().getAvgWaitingTime(),
                SJF.getStats().getAvgResponseTime());

        System.out.format("%10s %20.3f %20.3f %20.3f\n", "SRT",
                SRT.getStats().getAvgTurnaroundTime(),
                SRT.getStats().getAvgWaitingTime(),
                SRT.getStats().getAvgResponseTime());

        System.out.format("%10s %20.3f %20.3f %20.3f\n", "RR",
                RR.getStats().getAvgTurnaroundTime(),
                RR.getStats().getAvgWaitingTime(),
                RR.getStats().getAvgResponseTime());

        System.out.format("%10s %20.3f %20.3f %20.3f\n", "SJF",
                SJF.getStats().getAvgTurnaroundTime(),
                SJF.getStats().getAvgWaitingTime(),
                SJF.getStats().getAvgResponseTime());


        System.out.format("%10s %20.3f %20.3f %20.3f\n", "NP_HPF",
                NP_HPF.getStats().getAvgTurnaroundTime(),
                NP_HPF.getStats().getAvgWaitingTime(),
                NP_HPF.getStats().getAvgResponseTime());

        System.out.format("%10s %20.3f %20.3f %20.3f\n", "P_HPF",
                P_HPF.getStats().getAvgTurnaroundTime(),
                P_HPF.getStats().getAvgWaitingTime(),
                P_HPF.getStats().getAvgResponseTime());

        System.out.format("%10s %20.3f %20.3f %20.3f\n", "NP_HPF_AG",
                NP_HPF_AG.getStats().getAvgTurnaroundTime(),
                NP_HPF_AG.getStats().getAvgWaitingTime(),
                NP_HPF_AG.getStats().getAvgResponseTime());

        System.out.format("%10s %20.3f %20.3f %20.3f\n", "P_HPF_AG",
                P_HPF_AG.getStats().getAvgTurnaroundTime(),
                P_HPF_AG.getStats().getAvgWaitingTime(),
                P_HPF_AG.getStats().getAvgResponseTime());

    }
}
