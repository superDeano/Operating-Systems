import java.time.Instant;

public class Variable {
    int id;
    int value;
    Instant lastAccess;

    public Variable(int id, int value){
        lastAccess = Instant.now();
    }

    public int getId() {
        lastAccess = Instant.now();
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setLastAccess(Instant lastAccess) {
        this.lastAccess = lastAccess;
    }
}
