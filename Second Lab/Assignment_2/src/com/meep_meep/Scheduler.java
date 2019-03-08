package com.meep_meep;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Scheduler extends Thread {

    private int quantum;
    private List<String> log = new ArrayList<>();

    private Map<String, List<Process>> userProcessMap = new HashMap<>();
    private Map<Process, Integer> mapProcessTime = new HashMap<>();
    private List<Process> waitingProcesses = new ArrayList<>();
    private Queue<Process> readyQueue = new ArrayDeque<>();
    private BufferedWriter outputWriter;

    // Constructor
    Scheduler(int quantum) {
        this.quantum = quantum;

        //Creates an output stream to write output to file
        FileWriter outputFile = null;

        try {
            outputFile = new FileWriter("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputWriter = new BufferedWriter(outputFile);
    }

    // Function which adds all the process to the waitingQueue
    public void addProcess(Process process) {
        process.setObserverScheduler(this);
        process.setWriter(outputWriter);

        List<Process> list = userProcessMap.get(process.getUser());
        if (list == null) {
            list = new ArrayList<>();
            list.add(process);
            this.userProcessMap.put(process.getUser(), list);
            waitingProcesses.add(process);
        } else {
            list.add(process);
            //a List of all the processes we which are still waiting
            waitingProcesses.add(process);
        }
    }

    @Override
    /**
     * The function which runs it all. The for loop simulates the scheduler.
     * Checks the time each time for each process's status based on their time
     * */
    public void run() {
        setUpReadyQueue();

        Process currentProcess = null;
        Integer timeToStay = 0;

        for (int time = 1; true; time++) {
            checkWaitingProcesses(time);

            if(readyQueue.isEmpty() && currentProcess == null && waitingProcesses.isEmpty()){
                Scanner input = new Scanner(System.in);
                System.out.print("Most Probably done, exit? (y/n): ");
                String result = input.nextLine();
                if(result.toLowerCase().charAt(0) == 'y'){
                    System.exit(1);
                }
            }
            //Vu qu'on commence a un, on set up a zero
            if (time % quantum == 1) {
                System.out.println("**********NEW CYCLE**********");
                if(currentProcess != null) {
                    currentProcess.pause(time);
                    readyQueue.add(currentProcess);
                }

                divideTimePerProcess();
                currentProcess = readyQueue.poll();
                timeToStay = getTimeThreshold(time, currentProcess);
                currentProcess.start(time);

                try {
                    this.outputWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if(currentProcess == null) continue;

                //Checks what the process is supposed to be like
                ProcessStatus status = currentProcess.check(time);

                //Then decides what to do with the process based on its status
                if(status == ProcessStatus.FINISHED) {
                    userProcessMap.get(currentProcess.getUser()).remove(currentProcess);
                    currentProcess = readyQueue.poll();

                    if(currentProcess == null) {
                        //Last process
                        continue;
                    }
                    timeToStay = getTimeThreshold(time, currentProcess);
                    currentProcess.start(time);

                }else if(timeToStay == null){
                    //Runs till end of quantum (due to lack of decimal, time shares are rounded down.
                    // Thus, the currentProcess may be a process not from the next cycle. It will run until the cycle finishes)
                }else if(time >= timeToStay){
                    currentProcess.pause(time);
                    readyQueue.add(currentProcess);

                    currentProcess = readyQueue.poll();
                    timeToStay = getTimeThreshold(time, currentProcess);
                    currentProcess.start(time);
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

    /**
     *Function which calculates how many users have process which are ready.
     **/
    private int getNumberReadyUsers() {
        Set<String> readyUsers = new HashSet<>();
        for (Map.Entry<String, List<Process>> entry : userProcessMap.entrySet()) {
            Iterator<Process> it = entry.getValue().iterator();

            while (it.hasNext()) {
                Process p = it.next();
                if (p.getStatus() != ProcessStatus.WAITING && p.getStatus() != ProcessStatus.FINISHED) {
                    readyUsers.add(p.getUser());
                }
            }
        }
        return readyUsers.size();
    }

    /** This function sets up the scheduling:
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
                    this.waitingProcesses.remove(p);
                }
            }
        }
    }

    private Integer getTimeThreshold(int time, Process p){
        Integer allocatedTime = mapProcessTime.get(p);

        if(allocatedTime == null)
            return null;
        else
            return time + allocatedTime;
    }

    /**
     * Function divides the time each process for each user will have to run
     * */
    private void divideTimePerProcess() {
        mapProcessTime.clear();

        if (getNumberReadyUsers() == 0) return;

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
            mapProcessTime.put(process, timePerUser / numProcessForUser);
        }
    }

    /**
     * Function which put a process into the ready queue when it is ready
     * also removes it from the waiting queue
     * */

    /**
     * Function which check a process status at a given time
     * */
    private void checkWaitingProcesses(int time) {
        List<Process> toRemove = new ArrayList<>();

        for (Process p : waitingProcesses) {
            if(p.check(time) == ProcessStatus.READY){
                readyQueue.add(p);
                toRemove.add(p);
            }
        }
        waitingProcesses.removeAll(toRemove);
    }

    public List<String> getLog() {
        return log;
    }
}
