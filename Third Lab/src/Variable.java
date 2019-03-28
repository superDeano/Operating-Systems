import java.time.Instant;

public class Variable {
    Integer value;
    Instant lastAccess;

    public Variable(int value){
        this.value = value;
        lastAccess = Instant.now();
    }

    public int getValue() {
        lastAccess = Instant.now();
        return value;
    }

    public void setValue(int value) {
        lastAccess = Instant.now();
        this.value = value;
    }

    public Instant getLastAccess() {
        return lastAccess;
    }

}
