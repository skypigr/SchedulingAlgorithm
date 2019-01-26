package com.scu.coen383.team2.scheduling;

import java.util.PriorityQueue;
import java.util.Random;

public class ProcessGenerator {
    public static PriorityQueue<Process> generateJobs(int seed, int jobCnt) {

        PriorityQueue<Process> pq = new PriorityQueue<>();
        Random generator = new Random(seed);
        String names ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        // arrival time: 0 - 99
        // service time: 0 - 10
        int range_start = 0;
        int range_arrival = 99;
        int range_service = 10;
        int priority_bond = 4;

        for (int i = 0; i < jobCnt; i++) {
            float arrival_time = generator.nextFloat() * range_arrival; // nextFloat() [0,1)
            float service_time = generator.nextFloat() * range_service;
            int priority =     generator.nextInt(priority_bond);

            if ( service_time == 0) service_time += 1;

            pq.add(new Process(names.charAt(i), arrival_time, service_time, priority+1, 0));
        }

        return pq;
    }
}
