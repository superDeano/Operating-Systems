import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

public class Process {
    private int id;
    private int enterTime;
    private int duration;
    private int counter = 0;
    private final Object lock = new Object();
    private boolean paused = false;
    private BufferedWriter writer;

    private Thread thread = new Thread(() -> {
        while(true){
            synchronized (lock) {
                if(paused) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

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
        print(time);
    }

    public void start(int time) {

        thread.start();
        if(paused){
            synchronized (lock) {
                this.paused = false;
                lock.notify();
            }
        }

        print(time);
    }

    private void increment() {
        this.counter++;
    }

    private void print(int time) {
        try {
            this.writer.write("Time:" + time +", Process " + id);
        }catch (IOException e) {
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


}
