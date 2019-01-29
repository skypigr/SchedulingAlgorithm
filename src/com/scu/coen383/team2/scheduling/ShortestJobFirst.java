package com.scu.coen383.team2.scheduling;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ShortestJobFirst extends ScheduleBase{

    @Override
    public Queue<Process> schedule(PriorityQueue<Process> queue) {
        int startTime;
        int finishTime = 0;
        Process curProcess = null;
        Stats stats = this.getStats();

        // inputQueue is sorted based on the arrivalTime
        PriorityQueue<Process> inputQueue = new PriorityQueue<>((a, b) -> (int) (a.getArrivalTime() - b.getArrivalTime()));

        //readyQueue is sorted based on the burstTime, if the burstTime is same, it is sorted by the arrive time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>((a, b) -> {
            if (a.getServiceTime() == b.getServiceTime()) {
                return (int) (a.getArrivalTime() - b.getArrivalTime());
            }
            return (int) (a.getServiceTime() - b.getServiceTime());
        });

        //resultQueue is used to store all scheduled process
        Queue<Process> resultQueue = new LinkedList<>();

        while (!queue.isEmpty()) {
            curProcess = queue.poll();
            if (curProcess.getArrivalTime() <= 99) {
                inputQueue.offer(curProcess);
            }
        }
        executByTimeStep(inputQueue, readyQueue, resultQueue, stats);
        printTimeChart(resultQueue);
        printRoundAvg();
        stats.nextRound();

        return resultQueue;
    }
    public void executByTimeStep(PriorityQueue<Process> inputQueue, PriorityQueue<Process> readyQueue, Queue<Process> resultQueue, Stats stats) {
        // timeStamp is the starting moment of a time slice
        Process inExecutionProcess = null;
        int timeStamp = 0;
        while (inExecutionProcess != null || !inputQueue.isEmpty() || !readyQueue.isEmpty()) {
            if (!inputQueue.isEmpty() && inputQueue.peek().getArrivalTime() == timeStamp) {
                readyQueue.offer(inputQueue.poll());
            }
            if (inExecutionProcess == null) {
                if (!readyQueue.isEmpty() ) {
                    inExecutionProcess = readyQueue.poll();
                    calcuStas(timeStamp, inExecutionProcess, stats);
                    resultQueue.add(setScheduled(timeStamp, inExecutionProcess));
                }
            }
            if (inExecutionProcess != null){
                if (timeStamp >= inExecutionProcess.getStartTime() + inExecutionProcess.getServiceTime() + 1) {
                    inExecutionProcess = null;
                }
            }
            timeStamp++;
        }
    }
    //create Process which have been scheduled
    private Process setScheduled(int startTime, Process process) {
        return new Process(
                process.getName(),
                process.getArrivalTime(),
                process.getServiceTime(),
                process.getPriority(),
                startTime);
    }
    //add current scheduled Process into statistic
    public void calcuStas(int startTime, Process curProcess, Stats stats) {
        stats.addTurnaroundTime(curProcess.getServiceQuanta() + startTime - curProcess.getArrivalTime());     // finishTime - arrivalTime
        stats.addResponseTime(startTime - curProcess.getArrivalTime());                        // startTime - arrivalTime
        stats.addWaitTime(curProcess.getServiceQuanta() + startTime - curProcess.getArrivalTime() - curProcess.getServiceTime());// turnaroundTime - serviceTime
        stats.addProcess();
    }
}
