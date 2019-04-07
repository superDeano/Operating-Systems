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
    private boolean isKilled = false;
    private BufferedWriter writer;

    private Thread thread = new Thread(() -> {
        while (!isKilled) {
            Command com = CommandManager.getNextCommand();
            runCommand(com);
            int toWait = (int) (Math.random() * 1000 + 1);

            try {
                Thread.sleep(toWait);
            } catch (InterruptedException e) {
//                System.out.println(e.getCause());
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

    public boolean isRunning() {
        return threadStartedRunning;
    }

    private void finish(int time) {
        this.thread.interrupt();
        isKilled = true;
        threadStartedRunning = false;
        printFinish(time);
    }

    public void start(int time) {
        thread.start();
        printStart(time);
    }

    public void check(int time) {
        runtime = time;

        if (time == enterTime) {
            start(time);
            threadStartedRunning = true;
            counter += 100;
        } else if (counter == duration && threadStartedRunning) {
            finish(time);
        } else if (threadStartedRunning) { // Counter must be incremented only if thread is running
            counter += 100;
        } else {
            //Dead Process
        }

    }


    private void printAction(int time, Command command, Variable result) {
        try {
            // To print if swap has happened
            if (result != null) {
                Integer[] swapped = result.getSwapped();
                System.out.println("Clock: " + time + ", Memory Manager, SWAP: Variable " + swapped[Variable.oldVariable] + " with Variable " + swapped[Variable.newVariable]);
                this.writer.write("Clock: " + time + ", Memory Manager, SWAP: Variable " + swapped[Variable.oldVariable] + " with Variable " + swapped[Variable.newVariable]);
                writer.newLine();
            }

            System.out.println("Clock: " + time + ", Process " + id + ", " + command.getCommand() + ": Variable " + result.getId() + ((result.getValue() != null) ? ", Value:" + result.getValue() : ""));
            this.writer.write("Clock: " + time + ", Process " + id + ", " + command.getCommand() + ": Variable " + result.getId() + ((result.getValue() != null) ? ", Value:" + result.getValue() : ""));
            writer.newLine();
            this.writer.flush();
        } catch (IOException e) {
//            System.out.println("Cannot write for: Process " + id);
        }
    }

    private void printStart(int time) {
        try {
            System.out.println("Clock: " + time + ", Process " + id + ": Started.");
            this.writer.write("Clock: " + time + ", Process " + id + ": Started.");
            writer.newLine();
            this.writer.flush();
        } catch (IOException e) {
//            System.out.println("Cannot write for: Process " + id);
        }
    }

    private void printFinish(int time) {
        try {
            System.out.println("Clock: " + time + ", Process " + id + ": Finished.");
            this.writer.write("Clock: " + time + ", Process " + id + ": Finished.");
            writer.newLine();
            this.writer.flush();
        } catch (IOException e) {
//            System.out.println("Cannot write for: Process " + id);
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
        if (command == null) return;

        final int firstArgument = 0;
        final int secondArgument = 1;

        try {

            /**
             * Gets the command to execute from the command Manager
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
//            System.out.println("Cannot match method");
//            e.printStackTrace();
//            System.out.println(e.getCause());
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            System.out.println(e.getCause());
        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//            System.out.println(e.getCause());
        }
    }
}
