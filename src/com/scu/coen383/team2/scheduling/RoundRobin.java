package com.scu.coen383.team2.scheduling;


import java.util.*;

public class RoundRobin extends ScheduleBase {

    @Override
    public Queue<Process> schedule(PriorityQueue<Process> inputPQ) {
        if (inputPQ.isEmpty())
            return null;
        int startTime = 0;
        int finishTime = 0;
        Process oldProcess, scheduled, leftOver;
        Stats stats = this.getStats();
        Queue<Process> processQueue = new LinkedList<>();
        Map<Character, Integer> startTimeMap = new HashMap<>();
        Map<Character, Integer> finishTimeMap = new HashMap<>();
        Queue<Process> roundRobinQueue = new LinkedList<>();
        Queue<Process> roundRobinQueueRecycle = new LinkedList<>();

        while (!inputPQ.isEmpty() || !roundRobinQueue.isEmpty() || !roundRobinQueueRecycle.isEmpty()) {
            while (!inputPQ.isEmpty() && inputPQ.peek().getArrivalTime() <= finishTime)
                roundRobinQueue.add(inputPQ.poll());

            if (!roundRobinQueue.isEmpty()){
                oldProcess = roundRobinQueue.poll();
            } else if (!roundRobinQueueRecycle.isEmpty()){
                oldProcess = roundRobinQueueRecycle.poll();
            } else if (!inputPQ.isEmpty()){
                oldProcess = inputPQ.poll();
            } else {
                break;
            }

            Character oldProcessName = oldProcess.getName();

            startTime = Math.max((int) Math.ceil(oldProcess.getArrivalTime()), finishTime);
            finishTime = startTime + 1;

            if (!startTimeMap.containsKey(oldProcessName)) {
                if (startTime > 99)
                    break;
                startTimeMap.put(oldProcessName, startTime);
                stats.addWaitTime(startTime - oldProcess.getArrivalTime());
                stats.addResponseTime(startTime - oldProcess.getArrivalTime() + 1);
            } else {
                stats.addWaitTime(startTime - finishTimeMap.get(oldProcess.getName()));
            }

            //1 < server time: create new process minus 1 service time, put back into queue
            if (1 < oldProcess.getServiceTime()) {
                leftOver = new Process(oldProcess);//clone
                leftOver.setServiceTime(leftOver.getServiceTime() - 1);
                roundRobinQueueRecycle.add(leftOver);
                finishTimeMap.put(leftOver.getName(), finishTime);

            }
            // service time < 1
            else {
                stats.addTurnaroundTime(finishTime - oldProcess.getArrivalTime());
                stats.addProcess();
            }

            scheduled = setScheduled(startTime, oldProcess);
            scheduled.setServiceTime(1);
            //Process要改
//            statsState(startTime, finishTime, process, stats);
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

//    private void statsState(int startTime, int finishTime, Process process, Stats stats) {
//
//        stats.addTurnaroundTime(finishTime - process.getArrivalTime());                     // finishTime - arrivalTime
//        stats.addResponseTime(startTime - process.getArrivalTime());                        // startTime - arrivalTime
//        stats.addWaitTime(finishTime - process.getArrivalTime() - process.getServiceTime());// turnaroundTime - serviceTime
//        stats.addProcess();
//    }

}