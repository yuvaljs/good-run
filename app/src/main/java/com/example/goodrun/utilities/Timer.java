package com.example.goodrun.utilities;

public class Timer {
    private enum TimerStatus {neverStart, start, Pause, finish  };

    TimerStatus timerStatus = TimerStatus.neverStart;
    private long globalStartTime ,timerStartTime = 0,  timerPauseTime = 0 , timerStopTime ,globalStopTime = 0;

    public Timer() {
    }



    public long getTime(){
        long i = 0 ;
        switch (this.timerStatus)
        {
            case neverStart:
                return 0;
            case start:
                return   timerStartTime +System.currentTimeMillis()-globalStartTime;
            case Pause:
                return timerPauseTime;
            case finish:
                return  timerStopTime;

        }

        return i;
    }



    public void start(){
        if (!(timerStatus == TimerStatus.start || timerStatus == TimerStatus.finish)){
            globalStartTime = System.currentTimeMillis();
            timerStartTime = getTime();
            timerStatus = TimerStatus.start;
        }
    }

    public void pause(){
        if (timerStatus == TimerStatus.start)
        {
            timerPauseTime = getTime();
            timerStatus = TimerStatus.Pause;
        }
    }

    public void stop(){
        if(timerStatus == TimerStatus.start || timerStatus == TimerStatus.Pause){
            timerStopTime  = getTime();
            globalStopTime  = System.currentTimeMillis();
            timerStatus = TimerStatus.finish;
        }
    }

    public long getGlobalStopTime() {
        return globalStopTime;
    }
}
