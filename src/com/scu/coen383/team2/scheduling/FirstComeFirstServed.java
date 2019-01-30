package com.scu.coen383.team2.scheduling;


import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class FirstComeFirstServed extends ScheduleBase {

    @Override
    public Queue<Process> schedule(PriorityQueue<Process> queue) {

        int startTime;
        int finishTime = 0;
        int queueSize = queue.size();
        Process process;
        Process scheduled;
        Stats stats = this.getStats();
        Queue<Process> processQueue = new LinkedList<>(); // FIFO queue

        for (int i = 0; i < queueSize; ++i)
        {
            process = queue.remove();
            startTime = Math.max(process.getArrivalQuanta(), finishTime);
            finishTime = startTime + process.getServiceQuanta();

            if (startTime > 99)
                break;

            statsState(startTime, finishTime, process, stats);
            scheduled = setScheduled(startTime, process);
            processQueue.add(scheduled);
        }
        stats.addQuanta(finishTime);
        printTimeChart(processQueue);
        printRoundAvg();
        stats.nextRound();

        return processQueue;
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
