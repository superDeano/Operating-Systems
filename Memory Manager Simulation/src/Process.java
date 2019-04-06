import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class Process {
    private int id;
    private int enterTime;
    private int duration;
    private int runtime;
    private int counter = 0;
    private boolean threadStartedRunning = false;
    private BufferedWriter writer;

    private Thread thread = new Thread(() -> {
        while (true) {
            Command com = CommandManager.getNextCommand();
            runCommand(com); //TODO : This is never run
            int toWait = (int) (Math.random() * 1000 + 1);

            try {
                Thread.sleep(toWait);
            } catch (InterruptedException e) {
                System.out.println("Got killed during wait process[" + id + "]");
            }
        }
    });

    public Process(int id, int enterTime, int duration, BufferedWriter writer) {
        this.enterTime = enterTime;
        this.duration = duration;
        this.id = id;
        this.writer = writer;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    private void finish(int time) {
        this.thread.interrupt();
        threadStartedRunning = false;
        printFinish(time);
    }

    public void start(int time) {

        if (!threadStartedRunning) { //Thread will start only it was not running previously
            thread.start();
        }
        printStart(time);
    }

    public void check(int time) {
        runtime = time;

        if (time >= enterTime) {
            threadStartedRunning = true;
            start(time);
        } else if (counter >= duration) {
            finish(time);
        } else if (threadStartedRunning) { // Counter must be incremented only if thread is running
            counter += 50;
        }

    }

    private void printAction(int time, Command command, Variable result) {
        try {
            this.writer.write("Time:" + time + ", Process " + id + ", " + command.getCommand() + ": Variable" + result.getId() + ((result.getValue() != null) ? ", Value:" + result.getValue() : ""));
            System.out.println("Time:" + time + ", Process " + id + ", " + command.getCommand() + ": Variable" + result.getId() + ((result.getValue() != null) ? ", Value:" + result.getValue() : ""));
        } catch (IOException e) {
            System.out.println("Cannot write for: Process " + id);
        }
    }

    private void printStart(int time) {
        try {
            this.writer.write("Clock: " + time + ", Process " + id + ": Start");
            System.out.println("Clock: " + time + ", Process " + id + ": Start");
        } catch (IOException e) {
            System.out.println("Cannot write for: Process " + id);
        }
    }

    private void printFinish(int time) {
        try {
            this.writer.write("Clock: " + time + ", Process " + id + ": Finished");
            System.out.println("Clock: " + time + ", Process " + id + ": Finished");
        } catch (IOException e) {
            System.out.println("Cannot write for: Process " + id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return id == process.id &&
                enterTime == process.enterTime &&
                duration == process.duration &&
                counter == process.counter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterTime, duration, counter);
    }

    public void runCommand(Command command) {

        final int firstArgument = 0;
        final int secondArgument = 1;
        try {
            System.out.println("Trying to run command");
            /**Gets the command to execute from the command Manager
             * Use Reflection API to match that command to a memory manager's method
             * Executes the API call then
             * */
            switch (command.getArguments().length) {
                case 2:
                    Method task = MemoryManager.class.getDeclaredMethod(command.getCommand(), int.class, int.class);

                    Variable result = (Variable) task.invoke(null, command.getArguments()[firstArgument], command.getArguments()[secondArgument]);

                    printAction(runtime, command, result);
                    break;

                default:
                    task = MemoryManager.class.getDeclaredMethod(command.getCommand(), int.class);

                    result = (Variable) task.invoke(null, command.getArguments()[firstArgument]);

                    printAction(runtime, command, result);
            }


        } catch (NoSuchMethodException e) {
            System.out.println("Cannot match method");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        }
    }
}
