package com.meep_meep;

import java.util.*;

public class Scheduler extends Thread{

    private int quantum;
    private int currentTime = 1;

    private Map<User, Queue<Process>> userQueueMap = new HashMap<>();

    Scheduler(int quantum){
        this.quantum = quantum;
    }

    public void addUser(User user){
        this.userQueueMap.put(user, new ArrayDeque<>());
    }

    public void incrementTime(){
        this.quantum++;
    }

    public void incrementTime(int time){
        this.quantum += time;
    }

    @Override
    public void run() {

        setupQueue();

        while(true){
            
        }
    }

    public void setupQueue(){
        for(Map.Entry<User, Queue<Process>> entry : userQueueMap.entrySet()){
            for(Process p: entry.getKey().getProcesses()){
                if(p.getStatus().equals(ProcessStatus.READY)){
                    entry.getValue().add(p);
                }
            }
        }
    }

    private int getNumberReadyUsers(){
        int readyUsers = 0;
        for(Map.Entry<User, Queue<Process>> p : userQueueMap.entrySet()){
            if(!p.getValue().isEmpty())
                readyUsers++;
        }
        return readyUsers;
    }
}
