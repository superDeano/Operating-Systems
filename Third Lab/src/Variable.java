import java.time.Instant;

public class Variable {
    Integer id;
    Integer value;
    Instant lastAccess;

    public Variable(int id, int value){
        this.id = id;
        this.value = value;
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

}
