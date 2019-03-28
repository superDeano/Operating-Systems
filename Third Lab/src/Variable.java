import java.time.Instant;

public class Variable {
    Integer id;
    Integer value;
    Instant lastAccess;

    public Variable(int id, Integer value){
        this.id = id;
        this.value = value;
        lastAccess = Instant.now();
    }

    public Variable(String s){
        String[] temp = s.split("&");

        this.id = Integer.parseInt(temp[0]);
        this.value = Integer.parseInt(temp[1]);
        lastAccess = Instant.parse(temp[2]);
    }

    public int getId() {
        lastAccess = Instant.now();
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getValue() {
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

    @Override
    public String toString() {
        return id +"&" + value +"&" + lastAccess;
    }
}
