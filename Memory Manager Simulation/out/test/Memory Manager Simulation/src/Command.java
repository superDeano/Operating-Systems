public class Command {
    private String command;
    private Integer[] arguments;

    public Command(String command, Integer... arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer[] getArguments() {
        return arguments;
    }

    public void setArguments(Integer[] arguments) {
        this.arguments = arguments;
    }
}
