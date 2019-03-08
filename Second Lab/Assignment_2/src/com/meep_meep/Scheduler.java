package com.meep_meep;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class Scheduler extends Thread {

    private int quantum;
    private int numUsersWithProcessesReady = 0;
    private int timePerUser;
    private int numberOfUsers = 0;
    private Map<String, Queue<Process>> userQueueMap = new HashMap<>();
    private Queue<Process> readyProcesses = new ArrayDeque<>();

    Scheduler(int quantum) {
        this.quantum = quantum;
    }

    public void addProcess(Process process) {
        process.setObserverScheduler(this);
        Queue<Process> queue = userQueueMap.get(process.getUser());
        if(queue == null) {
            queue = new ArrayDeque<>();
            queue.add(process);
            this.userQueueMap.put(process.getUser(), queue);
            numberOfUsers++;
        }else{
            queue.add(process);
        }
    }

    public void incrementTime() {
        this.quantum++;
    }

    public void incrementTimeBy(int time) {
        this.quantum += time;
    }

    @Override
    public void run() {

        setupQueue();


        for (int time = 1, counter = 1; true; time = (time++) % quantum) {

            if (time == 1){
                //Set Up
            }
            else {
                //Execute threads
            }

        }
    }

    public void setupQueue() {
        for (Map.Entry<User, Queue<Process>> entry : userQueueMap.entrySet()) {
            for (Process p : entry.getKey().getProcesses()) {
                if (p.getStatus().equals(ProcessStatus.READY)) {
                    entry.getValue().add(p);
                }
            }
        }
    }

    private int getNumberReadyUsers() {
        int readyUsers = 0;
        for(Map.Entry<User, Queue<Process>> p : userQueueMap.entrySet()){
            if(!p.getValue().isEmpty())
                readyUsers++;
        }
        return readyUsers;
    }

    /* This function sets up the scheduling:
     * Starts by counting the number of users with ready processes
     * Then adds the processes which are ready in the readyQueue
     */
    private void setUp() {

        for(Queue<Process> u: userQueueMap.values()){
            Iterator<Process> it = u.iterator();
            while(it.hasNext()){
                Process p = it.next();
                if(p.getStatus() == ProcessStatus.READY){
                    this.readyProcesses.add(p);
                }
            }
        }

        timePerUser = quantum /numberOfUsers;
    }


    private void divideTimeForUserProcesses() {

        for (int i = 0; i < readyQueue.size(); i++) {

        }
    }

    void receiveProcess(Process p){

    }
}
