package com.scu.coen383.team2.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // write your code here
        PriorityQueue<Process> jobs = ProcessGenerator.generateJobs (123, 15);

        while (!jobs.isEmpty()) {
            System.out.println(jobs.poll());
        }
    }

}
