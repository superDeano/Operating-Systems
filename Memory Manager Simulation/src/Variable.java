import java.time.Instant;

public class Variable {
    Integer id;
    Integer value;
    Instant lastAccess;
    /**
     * Allows Process to know that a swap has taken place
     * */
    Integer [] swapped = null;
    public final static int oldVariable = 0;
    public final static int newVariable = 1;

    public Variable(int id, Integer value){
        this.id = id;
        this.value = value;
        lastAccess = Instant.now();
    }

    public void updateLastAccessed (){
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

    public void setSwapped(Integer[] swapped) {
        this.swapped = swapped;
    }

    public Integer[] getSwapped(){
        return swapped;
    }

    @Override
    public String toString() {
        return id +"&" + value +"&" + lastAccess;
    }
}
