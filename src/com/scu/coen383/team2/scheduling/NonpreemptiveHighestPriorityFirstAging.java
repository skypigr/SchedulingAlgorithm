package com.scu.coen383.team2.scheduling;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/*
    Ref: https://www.geeksforgeeks.org/program-for-preemptive-priority-cpu-scheduling/
         https://www.cs.rutgers.edu/~pxk/416/notes/07-scheduling.html

    After a process has waited for 5 quanta at a priority level, bump it up to next higher level.
    Process with same priority are scheduled by FCFS
 */

public class NonpreemptiveHighestPriorityFirstAging extends ScheduleBase {
    public Queue<Process> schedule(PriorityQueue<Process> initialQueue) {
        Queue<Process> scheduledQueue = new LinkedList<>();

        int finishTime = 0;
        int startTime;
        Process process;
        Process scheduled;
        ScheduleBase.Stats stats = getStats();

    /*
        readyQueues:
        [
            [...]
            [p0, p1, p2, ... pi]
            [...]
            [...]
         ]
         Each time, we pick up the first nonempty row as current Queue,
         which is the row with highest priority.
         When we do nonpreemptive HPF with Aging, we update each process in
         this readyQueues, increase their age by one, if some of them wait
         long enough, let's say it has waited for 5 quanta, we promote it
         to a higher priority level, put it to the end of [i-1] row.
     */
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
            finishTime = startTime + process.getServiceQuanta();

            if (startTime > 99) break;

            // build the timeline for current process, nonpreemptive
            for (int i = startTime; i < finishTime; i++) {
                // update priority of every process in readyQueues
                // upgrade it if necessary
                updatePriority(readyQueues);

                scheduled = setScheduled(i, 1, process);
                scheduledQueue.add(scheduled);
            }

            statsState(startTime, finishTime, process, stats);
        }

        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvg();
        stats.nextRound();

        return scheduledQueue;
    }

    private void updatePriority(Queue<Process>[] readyQueues) {
        for (int i = 0; i < MAX_PRIORITY; i++) {
            for (Process p: readyQueues[i]) {
                if (p.addAge() && i > 0) {
                    // here we are, get p out from current level
                    // then add it to higher level
                    readyQueues[i].remove(p);
                    readyQueues[i-1].add(p);
                }
            }
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

    private void statsState(int startTime, int finishTime, Process process, Stats stats) {

        stats.addTurnaroundTime(finishTime - process.getArrivalTime());                     // finishTime - arrivalTime
        stats.addResponseTime(startTime - process.getArrivalTime());                        // startTime - arrivalTime
        stats.addWaitTime(finishTime - process.getArrivalTime() - process.getServiceTime());// turnaroundTime - serviceTime
        stats.addProcess();
    }

    private int highestQueue(Queue<Process>[] queues) {

        for (int i = 0; i < MAX_PRIORITY; i++) {
            if (!queues[i].isEmpty()) return i;
        }
        return MAX_PRIORITY;
    }

}
