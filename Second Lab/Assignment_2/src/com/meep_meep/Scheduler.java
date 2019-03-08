package com.meep_meep;
import java.util.*;

public class Scheduler extends Thread {

    private int quantum;
    private int startingTime = 1;
    private Map<User, Queue<Process>> userQueueMap = new HashMap<>();
    //What i Did
    private int numUsersWithProcessesReady = 0;
    private int timePerUser;
    private ArrayList<Process> allProcesses = new ArrayList<>();
    private Set<String> usersWithReadyProcesses = new HashSet<>();
    private Queue<Process> readyQueue = new LinkedList<Process>();
//    private Map<String, int> timePerProcess = new ConcurrentMap<String, Integer>();

    Scheduler(int quantum) {
        this.quantum = quantum;
    }

    public void addUser(User user) {
        this.userQueueMap.put(user, new ArrayDeque<>());
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

        //Assume that number of users will not change
        for (int time = startingTime; true; time = (time++) % quantum) {

            if (time == 0){
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
//        for(Map.Entry<User, Queue<Process>> p : userQueueMap.entrySet()){
//            if(!p.getValue().isEmpty())
//                readyUsers++;
//        }
//        return readyUsers;
        for (int atIndex = 0; atIndex < allProcesses.size(); atIndex++) {
            if (allProcesses.get(atIndex).getStatus() == ProcessStatus.READY) {
//                usersWithReadyProcesses.add(allProcesses.get(atIndex).getUserID());
            }
        }
        return usersWithReadyProcesses.size();
    }

    /* The scheduler will get the list of all available process
     * Each process knows whats the user it comes from */
    private void addProcesses(Process process) {
        this.allProcesses.add(process);
    }

    /* This function sets up the scheduling:
     * Starts by counting the number of users with ready processes
     * Then adds the processes which are ready in the readyQueue
     */
    private void setUp() {

        for (int atIndex = 0; atIndex < allProcesses.size(); atIndex++) {
            if (allProcesses.get(atIndex).getStatus() == ProcessStatus.READY) {
//                usersWithReadyProcesses.add(allProcesses.get(atIndex).getUserID());
                readyQueue.add(allProcesses.get(atIndex));
            }
        }

        timePerUser = quantum / usersWithReadyProcesses.size();
    }


    private void divideTimeForUserProcesses() {

        for (int i = 0; i < readyQueue.size(); i++) {

        }
    }
}
