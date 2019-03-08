package com.meep_meep;

import java.util.Objects;

public class Process {
    private int id;
    private ProcessStatus status;
    private int enterTime;
    private int duration;
    private int counter = 0;
    private User user;
    private final Object lock = new Object();
    private boolean paused = false;
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

    public Process(User user, int id, int enterTime, int duration) {
        this.enterTime = enterTime;
        this.duration = duration;
        this.user = user;
        this.id = id;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public void finish() {
        this.status = ProcessStatus.FINISHED;
        this.thread.interrupt();
        print();
    }

    public void pause() {
        this.status = ProcessStatus.PAUSED;
        this.paused = true;
        print();
    }

    private void resume(boolean start) {
        this.status = ProcessStatus.RESUMED;

        if(start){
            thread.start();
        }else{
            synchronized (lock) {
                this.paused = false;
                lock.notify();
            }
        }

        print();
    }

    public void start() {

        if(this.status == ProcessStatus.READY){
            this.resume(true);
        }else{
            this.resume(false);
        }

        this.status = ProcessStatus.STARTED;
        print();

    }


    public void ready() {
        this.status = ProcessStatus.READY;
    }

    private void increment() {
        this.counter++;
    }

    private String print() {
        return ("User " + user.getName() + ", Process " + id + ", " + this.status);
    }

    public ProcessStatus check(int i) {

        if (i == enterTime) {
            this.status = ProcessStatus.READY;
        } else if (counter == duration) {
            this.status = ProcessStatus.FINISHED;
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
