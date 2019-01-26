package com.scu.coen383.team2.scheduling;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

// Nonpreemptive HPF without aging
// process with same priority are scheduled by FCFS
public class NonpreemptiveHighestPriorityFirst extends ScheduleBase {

    @Override
    public Queue<Process> schedule(PriorityQueue<Process> initialQueue){
        Queue<Process> scheduledQueue = new LinkedList<>();

        int finishTime = 0;
        int startTime;
        Process process, scheduled;
        ScheduleBase.Stats stats = this.getStats();

        //rank by priority in ascending order
        PriorityQueue<Process> readyQueue = new PriorityQueue<>((Comparator.comparingInt(Process::getPriority)));

        while (!initialQueue.isEmpty() || ! readyQueue.isEmpty()) {
            // fectch ready process from initalQueue, put them into ready Queue, waiting for execution
            while (! initialQueue.isEmpty() && initialQueue.peek().getArrivalQuanta() <= finishTime) {
//                System.out.format("Add: %s, arrivalTime: %5.2f, finishTime: %2d\n",
//                        initialQueue.peek().getName(),
//                        initialQueue.peek().getArrivalTime(),
//                        finishTime);

                readyQueue.add(initialQueue.poll());
            }

            process = readyQueue.isEmpty() ? initialQueue.poll() : readyQueue.poll();
            startTime = Math.max(process.getArrivalQuanta(), finishTime);
            finishTime = startTime + process.getServiceQuanta();

            if (startTime > 99) break;

            statsState(startTime, finishTime, process, stats);
            scheduled = setScheduled(startTime, process);

            scheduledQueue.add(scheduled);
        }

        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvg();
        stats.nextRound();


        return scheduledQueue;
    }

    private Process setScheduled(int startTime, Process process) {
        return new Process(
                process.getName(),
                process.getArrivalTime(),
                process.getServiceTime(),
                process.getPriority(),
                startTime);
    }

    private void statsState(int startTime, int finishTime, Process process, Stats stats) {

        stats.addTurnaroundTime(finishTime - process.getArrivalTime());                     // finishTime - arrivalTime
        stats.addResponseTime(startTime - process.getArrivalTime());                        // startTime - arrivalTime
        stats.addWaitTime(finishTime - process.getArrivalTime() - process.getServiceTime());// turnaroundTime - serviceTime
        stats.addProcess();
    }
}
