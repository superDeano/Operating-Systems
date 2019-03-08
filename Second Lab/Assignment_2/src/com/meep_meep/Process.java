package com.meep_meep;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

public class Process {
    private int id;
    private ProcessStatus status;
    private int enterTime;
    private int duration;
    private int counter = 0;
    private String user;
    private final Object lock = new Object();
    private boolean paused = false;
    private Scheduler observerScheduler;
    private BufferedWriter writer;

    private Thread thread = new Thread(() -> {
        while(true){
            synchronized (lock) {
                if(paused) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    });

    public Process(String user, int id, int enterTime, int duration, BufferedWriter writer) {
        this.enterTime = enterTime;
        this.duration = duration;
        this.user = user;
        this.id = id;
        this.writer = writer;

        if (enterTime == 1) {
            this.status = ProcessStatus.READY;
        } else {
            this.status = ProcessStatus.WAITING;
        }
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public int getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(int enterTime) {
        this.enterTime = enterTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public Scheduler getObserverScheduler() {
        return observerScheduler;
    }

    public void setObserverScheduler(Scheduler observerScheduler) {
        this.observerScheduler = observerScheduler;
    }

    private void finish(int time) {
        this.status = ProcessStatus.FINISHED;
        this.thread.interrupt();
        print(time);
    }

    public void pause(int time) {
        this.status = ProcessStatus.PAUSED;
        this.paused = true;
        print(time);
    }

    private void resume(boolean start, int time) {
        this.status = ProcessStatus.RESUMED;

        if(start){
            thread.start();
        }else{
            synchronized (lock) {
                this.paused = false;
                lock.notify();
            }
        }

        print(time);
    }

    public void start(int time) {

        if(this.status == ProcessStatus.READY){
            this.status = ProcessStatus.STARTED;
            print(time);
            this.resume(true, time);
        }else{
            this.resume(false, time);
        }


    }


    public void ready() {
        this.status = ProcessStatus.READY;
    }

    private void increment() {
        this.counter++;
    }

    private void print(int time) {
        try {
            this.writer.write("Time:" + time +", User " + user + ", Process " + id + ", " + this.status+"\n");
        }catch (IOException e) {
            System.out.println("Cannot write for: " + "User " + user + ", Process " + id);
        }
    }

    public ProcessStatus check(int time) {

        if (time == enterTime) {
            this.status = ProcessStatus.READY;
        } else if (counter == duration) {
            this.status = ProcessStatus.FINISHED;
            finish(time);
        } else if (status == ProcessStatus.RESUMED) {
            increment();
        }

        return this.status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return id == process.id &&
                enterTime == process.enterTime &&
                duration == process.duration &&
                counter == process.counter &&
                status == process.status &&
                Objects.equals(user, process.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, enterTime, duration, counter, user);
    }


}
