package com.scu.coen383.team2.scheduling;

public class Process implements Comparable<Process> {

    private char _name;             // pcocess name
    private int _priority;          // priority 1,2,3,4
    private float _arrivalTime;    // arrival time 0 - 99
    private int _startTime;         // start time
    private float _serviceTime;    // expected total run time 0.1 - 10

    public int getPriority()        { return _priority; }
    public char getName()           { return _name; }
    public float getArrivalTime()   { return _arrivalTime; }
    public float getServiceTime()   { return _serviceTime; }
    public int getServiceQuanta()   {return (int)Math.ceil(_serviceTime);}
    public int getStartTime()       { return _startTime; }




    Process(char name, float arrival_time, float service_time, int priority, int start_time) {
        _name = name;
        _arrivalTime = arrival_time;
        _serviceTime = service_time;
        _priority = priority;
        _startTime = start_time;
    }


    @Override
    public int compareTo(Process other) {
        return Float.compare(_arrivalTime, other._arrivalTime);
    }

    @Override
    public String toString()
    {
        return String.format("    Process %c [arrivalTime= %5.2f, expectedRunTime= %4.2f, priority= %d]",
                _name, _arrivalTime, _serviceTime, _priority);
    }
}
