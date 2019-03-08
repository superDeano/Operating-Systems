package com.meep_meep;

import java.util.*;

public class Scheduler extends Thread {

    private int quantum;
    private int numUsersWithProcessesReady = 0;
    private int numberOfUsers = 0;

    private Map<String, Queue<Process>> userQueueMap = new HashMap<>();
    private Map<Integer, Integer> mapProcessTime = new HashMap<>();

    private Queue<Process> readyQueue = new ArrayDeque<>();

    Scheduler(int quantum) {
        this.quantum = quantum;
    }

    public void addProcess(Process process) {
        process.setObserverScheduler(this);
        Queue<Process> queue = userQueueMap.get(process.getUser());
        if (queue == null) {
            queue = new ArrayDeque<>();
            queue.add(process);
            this.userQueueMap.put(process.getUser(), queue);
            numberOfUsers++;
        } else {
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
        setUpReadyQueue();
        int elementsToPop = 0;
        for (int time = 1, counter = 1; true; time = (time++) % quantum) {

            if (time == 1) {
                //Set Up
            } else {
                //Execute threads
            }

        }
    }

    private int getNumberReadyUsers() {
        Set<String> readyUsers = new HashSet<>();
        for (Map.Entry<String, Queue<Process>> entry : userQueueMap.entrySet()) {
            Iterator<Process> it = entry.getValue().iterator();

            while (it.hasNext()) {
                Process p = it.next();
                if (p.getStatus() == ProcessStatus.READY) {
                    readyUsers.add(p.getUser());
                }
            }
        }
        return readyUsers.size();
    }

    /* This function sets up the scheduling:
     * Starts by counting the number of users with ready processes
     * Then adds the processes which are ready in the readyQueue
     */
    private void setUpReadyQueue() {
        for (Queue<Process> u : userQueueMap.values()) {
            Iterator<Process> it = u.iterator();
            while (it.hasNext()) {
                Process p = it.next();
                if (p.getStatus() == ProcessStatus.READY) {
                    this.readyQueue.add(p);
                }
            }
        }
    }

    void setupForQuantumCycle() {

    }

    private void divideTimePerProcess() {
        int counter = 0;
        int timePerUser = quantum / getNumberReadyUsers();
        Map<String, Integer> mapUserNumReadyProcess = new HashMap<>();

        for (Process process : readyQueue) {
            Integer temp = mapUserNumReadyProcess.get(process.getUser());

            if(temp == null){
                mapUserNumReadyProcess.put(process.getUser(), 1);
            }else{
                mapUserNumReadyProcess.put(process.getUser(), temp + 1);
            }
        }

        int numProcessForUser;
        for (Process process : readyQueue) {
            numProcessForUser = mapUserNumReadyProcess.get(process.getUser());
            mapProcessTime.put(process.getId(), timePerUser / numProcessForUser);
        }

    }

//    void receiveProcess(Process p) {
//        this.readyQueue.add(p);
//    }
}
