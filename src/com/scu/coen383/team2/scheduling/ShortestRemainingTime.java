package com.scu.coen383.team2.scheduling;

import java.util.*;


public class ShortestRemainingTime extends ScheduleBase {

    @Override
    public Queue<Process> schedule(PriorityQueue<Process> q)
    {
        int finishTime = 0;
        int startTime;
        Process p;
        Process scheduled;
        Process remaining;
        Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();


        Map<Character, Integer> startTimes = new HashMap<>();
        Map<Character, Integer> finishTimes = new HashMap<>();

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(new Comparator<Process>(){
            @Override
            public int compare(Process p1, Process p2)
            {
                if (p1.getServiceQuanta() == p2.getServiceQuanta())
                    return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                else
                    return p1.getServiceQuanta() < p2.getServiceQuanta() ? -1 : 1;
            }
        });


        PriorityQueue<Process> waitingQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2)
            {
                if (p1.getServiceQuanta() == p2.getServiceQuanta())
                    return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                else
                    return p1.getServiceQuanta() < p2.getServiceQuanta() ? -1 : 1;
            }
        });

        while (!q.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {

            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());


            //顺序：Waiting > Ready > Q
            if (readyQueue.isEmpty())
                p = (waitingQueue.isEmpty()) ? q.poll() : waitingQueue.poll();
            else if (waitingQueue.isEmpty())
                p = readyQueue.poll();
            else
                p = (readyQueue.peek().getServiceQuanta() < waitingQueue.peek().getServiceQuanta())
                        ? readyQueue.poll()
                        : waitingQueue.poll();

            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);
            finishTime = startTime + 1;


            if (!startTimes.containsKey(p.getName()))
            {
                if (startTime > 99)
                    break;
                startTimes.put(p.getName(), startTime);
                stats.addWaitTime(startTime - p.getArrivalTime());
                stats.addResponseTime(startTime - p.getArrivalTime());
            }
            else
                stats.addWaitTime(startTime - finishTimes.get(p.getName()));


            if (p.getServiceQuanta() > 1)
            {

                remaining = new Process(p);
                remaining.setServiceTime(remaining.getServiceQuanta() - 1);
                waitingQueue.add(remaining);
                finishTimes.put(remaining.getName(), finishTime);
            }
            else
            {
                stats.addTurnaroundTime(finishTime - startTimes.get(p.getName()));
                stats.addProcess();
            }

            scheduled = new Process(p);
            scheduled.setServiceTime(1);
            scheduled.setStartTime(startTime);
            scheduled.setName(p.getName());
            scheduledQueue.add(scheduled);
        }
        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvg();
        stats.nextRound();

        return scheduledQueue;
    }


}

