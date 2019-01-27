package com.scu.coen383.team2.scheduling;

public class Process implements Comparable<Process> {

    private char    _name;              // pcocess name
    private int     _priority;          // priority 1,2,3,4
    private float   _arrivalTime;       // arrival time 0 - 99
    private int     _startTime;         // start time
    private float   _serviceTime;       // expected total run time 0.1 - 10
    private int     _age;

    public int      getPriority()       { return _priority; }
    public char     getName()           { return _name; }
    public float    getArrivalTime()    { return _arrivalTime; }
    public float    getServiceTime()    { return _serviceTime; }
    public int      getServiceQuanta()  { return (int)Math.ceil(_serviceTime);}
    public int      getArrivalQuanta()  { return (int)Math.ceil((_arrivalTime));}
    public int      getStartTime()      { return _startTime; }
    public int      getAge()            { return _age;}

    public void     setServiceTime(float newServiceTime)    { _serviceTime = newServiceTime; }

    public void     addAge() {
        _age += 1;
        if ( _priority > 1 && 5 == _age) {
            _priority -= 1;
            _age = 0;
        }
    }

    Process(char name, float arrival_time, float service_time, int priority, int start_time) {
        _name = name;
        _arrivalTime = arrival_time;
        _serviceTime = service_time;
        _priority = priority;
        _startTime = start_time;
        _age  = 0;
    }

    Process(Process obj) {
        _name = obj.getName();
        _arrivalTime    = obj.getArrivalTime();
        _serviceTime    = obj.getServiceTime();
        _priority       = obj.getPriority();
        _startTime      = obj.getStartTime();
        _age            = obj.getAge();
    }


    @Override
    public int compareTo(Process other) {
        return Float.compare(_arrivalTime, other._arrivalTime);
    }

    @Override
    public String toString()
    {
        return String.format("    Process %c [arrivalTime= %5.2f, expectedRunTime= %4.2f, priority= %d]",
                _name,
                _arrivalTime,
                _serviceTime,
                _priority);
    }
}
