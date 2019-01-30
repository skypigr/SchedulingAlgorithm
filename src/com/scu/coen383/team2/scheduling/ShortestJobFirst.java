package com.scu.coen383.team2.scheduling;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

// SJF scheduling
// process with same service time are scheduled by FCFS
public class ShortestJobFirst extends ScheduleBase{

    @Override
    public Queue<Process> schedule(PriorityQueue<Process> inputQueue) {
        Queue<Process> resultQueue = new LinkedList<>();

        int finishTime = 0;
        int startTime;
        Process process;
        Process scheduled;
        Stats stats = this.getStats();

        // readyQueue is sorted by service time
        // if they have same service time, sorted them by the arrive time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>((a, b) -> a.getServiceTime() == b.getServiceTime()
                ? Float.compare(a.getArrivalTime(), b.getArrivalTime())
                : Float.compare(a.getServiceTime(), b.getServiceTime()));

        while (!inputQueue.isEmpty() || ! readyQueue.isEmpty()) {
            // fectch ready process from inputQueue, put them into readyQueue, waiting for execution
            while (!inputQueue.isEmpty() && inputQueue.peek().getArrivalQuanta() <= finishTime) {
                readyQueue.add(inputQueue.poll());
            }

            process = readyQueue.isEmpty() ? inputQueue.poll() : readyQueue.poll();
            startTime = Math.max(process.getArrivalQuanta(), finishTime);
            finishTime = startTime + process.getServiceQuanta();

            if (startTime > 99) break;

            statsState(startTime, finishTime, process, stats);
            scheduled = setScheduled(startTime, process.getServiceTime(), process);

            resultQueue.add(scheduled);
        }

        stats.addQuanta(finishTime);
        printTimeChart(resultQueue);
        printRoundAvg();
        stats.nextRound();

        return resultQueue;
    }

    private Process setScheduled(int startTime, float serviceTime, Process process) {
        return new Process(
                process.getName(),
                process.getArrivalTime(),
                serviceTime,
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
