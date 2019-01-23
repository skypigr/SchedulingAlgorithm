package com.scu.coen383.team2.scheduling;

public class Process implements Comparable<Process> {

    private char _name;
    private int _arrivalTime;
    private int _startTime;
    private int _serviceTime;
    private int _priority;

    public int getArrivalTime() { return _arrivalTime; }
    public int getServiceTime() { return _serviceTime; }
    public int getPriority()    { return _priority; }
    public int getStartTime()   { return _startTime; }
    public char getName()       { return _name; }


    Process(char name, int arrival_time, int service_time, int priority, int start_time) {
        _name = name;
        _arrivalTime = arrival_time;
        _serviceTime = service_time;
        _priority = priority;
        _startTime = start_time;
    }


    @Override
    public int compareTo(Process other) {
        return _arrivalTime < other._arrivalTime ? -1 : _arrivalTime > other._arrivalTime ? 1 : 0;
    }

    @Override
    public String toString()
    {
        return String.format("    Process %c [arrivalTime= %2d, expectedRunTime= %2d, priority= %d]",
                _name, _arrivalTime, _serviceTime, _priority);
    }
}
