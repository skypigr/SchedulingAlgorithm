package com.scu.coen383.team2.scheduling;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/*
    Ref: https://www.geeksforgeeks.org/program-for-preemptive-priority-cpu-scheduling/
         https://www.cs.rutgers.edu/~pxk/416/notes/07-scheduling.html

    Use RR with time slice of 1 quantum for each priority queue
 */

public class PreemptiveHighestPriorityFirst extends ScheduleBase {

    public Queue<Process> schedule(PriorityQueue<Process> initialQueue) {
        Queue<Process> scheduledQueue = new LinkedList<>();

        int finishTime = 0;
        int startTime;
        Process process;
        Process scheduled;
        ScheduleBase.Stats stats = this.getStats();

        HashMap<Character, Float> arrivalTimeTable = new HashMap<>(); // unqiue for every process
        HashMap<Character, Integer> finishTimeTable = new HashMap<>(); // update each round

        Queue<Process>[] readyQueues = new Queue[MAX_PRIORITY];
        for (int i = 0; i < MAX_PRIORITY ; i++) {
            readyQueues[i] = new LinkedList<>();
        }

        int curQueueIndex = MAX_PRIORITY;
        while (curQueueIndex < MAX_PRIORITY || !initialQueue.isEmpty()) {

            // fetch process from initialQueue and put them into corresponding readyQueue
            while (!initialQueue.isEmpty() && initialQueue.peek().getArrivalQuanta() <= finishTime) {
                readyQueues[initialQueue.peek().getPriority() - 1].add(initialQueue.poll());
            }

            curQueueIndex = highestQueue(readyQueues);

            // both of readyQueue and initialQueue are empty, we are done
            if (curQueueIndex == MAX_PRIORITY && initialQueue.isEmpty()) break;

            process = curQueueIndex < MAX_PRIORITY ? readyQueues[curQueueIndex].poll() : initialQueue.poll();
            startTime = Math.max(process.getArrivalQuanta(), finishTime);
            finishTime = startTime + 1;


//            System.out.format("Index: %2d, Name: %c, finishTime: %2d, left: %d, %s\n",
//                    curQueueIndex,
////                    readyQueues[curQueueIndex].size(),
//                    process.getName(),
//                    finishTime,
//                    initialQueue.size(),
//                    process);


            // update stats
            updateStats(arrivalTimeTable, finishTimeTable, readyQueues, startTime, finishTime, process, stats);
            scheduled = setScheduled(startTime, 1, process);
            scheduledQueue.add(scheduled);
        }

        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvg();
        stats.nextRound();


        return scheduledQueue;
    }

    private void updateStats(HashMap<Character, Float> arrivalTimeTable,
                             HashMap<Character, Integer> finishTimeTable,
                             Queue<Process>[] readyQueues,
                             int startTime,
                             int finishTime,
                             Process process,
                             ScheduleBase.Stats stats) {
        if (!arrivalTimeTable.containsKey(process.getName()))   {
            if (startTime < 100) {
                arrivalTimeTable.put(process.getName(), process.getArrivalTime());
                stats.addResponseTime(startTime - process.getArrivalTime());
                stats.addWaitTime(startTime - process.getArrivalTime());
            }
        } else {
            stats.addWaitTime(startTime - finishTimeTable.get(process.getName()));
        }

        // still have more than 1 quanta to run
        if (process.getServiceQuanta() > 1) {

            Process remaining = new Process(process);
            remaining.setServiceTime(remaining.getServiceTime()-1);
            readyQueues[remaining.getPriority()-1].add(remaining);
            finishTimeTable.put(remaining.getName(), finishTime);

        } else {
            // current process terminate
            stats.addTurnaroundTime(finishTime - process.getArrivalTime());
            stats.addProcess();
        }

    }
    private Process setScheduled(int startTime, float serviceTime, Process process) {
        return new Process(
                process.getName(),
                process.getArrivalTime(),
                serviceTime,
                process.getPriority(),
                startTime);
    }

    private int highestQueue(Queue<Process>[] queues) {

        for (int i = 0; i < MAX_PRIORITY; i++) {
            if (!queues[i].isEmpty()) return i;
        }
        return MAX_PRIORITY;
    }
}
