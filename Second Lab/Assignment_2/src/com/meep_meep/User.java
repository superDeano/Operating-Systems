package com.meep_meep;

import java.util.Arrays;
import java.util.Objects;

public class User {
    private int numberOfProcess;
    private String name;
    private Process[] processes;

    public User(String name, int numberOfProcess){
        this.numberOfProcess = numberOfProcess;
        this.processes = new Process[numberOfProcess];
        this.name = name;
    }

    public int getNumberOfProcess() {
        return numberOfProcess;
    }

    public void setNumberOfProcess(int numberOfProcess) {
        this.numberOfProcess = numberOfProcess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Process[] getProcesses() {
        return processes;
    }

    public void setProcesses(Process[] processes) {
        this.processes = processes;
    }

    public void setScheduler(Scheduler scheduler) {
        for(Process p: processes){
            p.setObserverScheduler(scheduler);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return numberOfProcess == user.numberOfProcess &&
                Objects.equals(name, user.name) &&
                Arrays.equals(processes, user.processes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numberOfProcess, name);
        result = 31 * result + Arrays.hashCode(processes);
        return result;
    }
}
