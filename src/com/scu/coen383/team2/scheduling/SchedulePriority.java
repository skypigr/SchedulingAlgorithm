package com.scu.coen383.team2.scheduling;

import java.util.*;

public abstract class SchedulePriority extends ScheduleBase {
     protected void updateStats(HashMap<Character, Float> arrivalTimeTable,
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

    protected void updatePriority(Queue<Process>[] readyQueues) {
        for (int i = 0; i < MAX_PRIORITY; i++) {

            List<Process> tempList = new ArrayList<>();
            for (Process p: readyQueues[i]) {
                if (p.addAge() && i > 0) {
                    // here we are, get p out from current level
                    // then add it to higher level
                    // readyQueues[i].remove(p);
                    tempList.add(p);
                    readyQueues[i-1].add(p);
                }
            }

            // remove from current level
            for (Process rmP: tempList) {
                readyQueues[i].remove(rmP);
            }

        }
    }
    protected void statsState(int startTime, int finishTime, Process process, Stats stats) {

        stats.addTurnaroundTime(finishTime - process.getArrivalTime());                     // finishTime - arrivalTime
        stats.addResponseTime(startTime - process.getArrivalTime());                        // startTime - arrivalTime
        stats.addWaitTime(finishTime - process.getArrivalTime() - process.getServiceTime());// turnaroundTime - serviceTime
        stats.addProcess();
    }

    protected Process setScheduled(int startTime, float serviceTime, Process process) {
        return new Process(
                process.getName(),
                process.getArrivalTime(),
                serviceTime,
                process.getPriority(),
                startTime);
    }

    protected int highestQueue(Queue<Process>[] queues) {

        for (int i = 0; i < MAX_PRIORITY; i++) {
            if (!queues[i].isEmpty()) return i;
        }
        return MAX_PRIORITY;
    }
}
