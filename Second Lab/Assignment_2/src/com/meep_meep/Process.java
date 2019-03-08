package com.meep_meep;

import java.util.Objects;

public class Process {
    private int id;
    private ProcessStatus status;
    private int enterTime;
    private int duration;
    private int counter = 0;
    private User user;

    private Thread thread = new Thread(() -> {
        while(true){
            System.out.print("he");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    public void pause() {
        this.status = ProcessStatus.PAUSED;
        try {
            this.thread.wait();
        } catch (InterruptedException e) {
           System.out.println("User: " + this.user.getName() + " " + this.id + " -> cannot wait (oupsi daisy)");
        }
        print();
    }

    public void resume() {
        this.status = ProcessStatus.RESUMED;
        print();
    }

    public void start() {
        if(this.status == ProcessStatus.READY){
            thread.start();
        }else{
            thread.notify();
        }
        this.status = ProcessStatus.STARTED;
        print();
        this.resume();
    }

    public void finish() {
        this.status = ProcessStatus.FINISHED;
        this.thread.interrupt();
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
