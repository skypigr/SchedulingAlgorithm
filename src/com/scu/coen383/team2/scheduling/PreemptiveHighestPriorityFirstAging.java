package com.scu.coen383.team2.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/*
    Ref: https://www.geeksforgeeks.org/program-for-preemptive-priority-cpu-scheduling/
         https://www.cs.rutgers.edu/~pxk/416/notes/07-scheduling.html

    After a process has waited for 5 quanta at a priority level, bump it up to next higher level.
    Use RR with time slice of 1 quantum for each priority queue
 */

public class PreemptiveHighestPriorityFirstAging extends SchedulePriority{
    public Queue<Process> schedule(PriorityQueue<Process> initialQueue) {
        Queue<Process> scheduledQueue = new LinkedList<>();

        int finishTime = 0;
        int startTime;
        Process process;
        Process scheduled;
        ScheduleBase.Stats stats = this.getStats();

        HashMap<Character, Float>   arrivalTimeTable    = new HashMap<>(); // unique for every process
        HashMap<Character, Integer> finishTimeTable     = new HashMap<>(); // update each round

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
            finishTime = startTime + 1;

            // update priority
            updatePriority(readyQueues);

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

}
