package com.meep_meep;

import java.util.*;

public class Scheduler extends Thread {

    private int quantum;
    private List<String> log = new ArrayList<>();

    private Map<String, List<Process>> userProcessMap = new HashMap<>();
    private Map<Integer, Integer> mapProcessTime = new HashMap<>();
    private List<Process> waitingProcesses = new ArrayList<>();
    private Queue<Process> readyQueue = new ArrayDeque<>();


    Scheduler(int quantum) {
        this.quantum = quantum;
    }

    public void addProcess(Process process) {
        process.setObserverScheduler(this);
        List<Process> list = userProcessMap.get(process.getUser());
        if (list == null) {
            list = new ArrayList<>();
            list.add(process);
            this.userProcessMap.put(process.getUser(), list);
            waitingProcesses.add(process);
        } else {
            list.add(process);
            //a List of all the processes we have
            waitingProcesses.add(process);
        }
    }

    @Override
    public void run() {
        setUpReadyQueue();

        Process currentProcess = null;
        int timeToStay = 0;

        for (int time = 1; true; time++) {
            checkWaitingProcesses(time);

            //Vu qu'on commence a un, on set up a zero
            if (time == 1) {
                // on check le nombre de process ready
                //on divide le temps pour chaque process accordingly
                setupForQuantumCycle();
                currentProcess = readyQueue.poll();
                timeToStay = mapProcessTime.get(currentProcess.getId()) + time;
                currentProcess.start();
            } else {
                ProcessStatus status = currentProcess.check(time);

                if(status == ProcessStatus.FINISHED){
                    userProcessMap.get(currentProcess.getUser()).remove(currentProcess);
                    currentProcess = readyQueue.poll();
                    timeToStay = mapProcessTime.get(currentProcess.getId()) + time;
                }else if(time >= timeToStay){
                    currentProcess.pause();
                    readyQueue.add(currentProcess);
                    currentProcess = readyQueue.poll();
                }else{
                    //Runs
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private int getNumberReadyUsers() {
        Set<String> readyUsers = new HashSet<>();
        for (Map.Entry<String, List<Process>> entry : userProcessMap.entrySet()) {
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
        for (List<Process> u : userProcessMap.values()) {
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
        mapProcessTime.clear();

        int timePerUser = quantum / getNumberReadyUsers();
        Map<String, Integer> mapUserNumReadyProcess = new HashMap<>();

        for (Process process : readyQueue) {
            Integer temp = mapUserNumReadyProcess.get(process.getUser());

            if (temp == null) {
                mapUserNumReadyProcess.put(process.getUser(), 1);
            } else {
                mapUserNumReadyProcess.put(process.getUser(), temp + 1);
            }
        }

        int numProcessForUser;
        for (Process process : readyQueue) {
            numProcessForUser = mapUserNumReadyProcess.get(process.getUser());
            mapProcessTime.put(process.getId(), timePerUser / numProcessForUser);
        }

    }

    public void receiveProcess(Process process) {
        this.readyQueue.add(process);
        waitingProcesses.remove(process);
    }

    private void checkWaitingProcesses(int time) {
        for (Process p : waitingProcesses) {
            p.check(time);
        }
    }

    public List<String> getLog() {
        return log;
    }
}
