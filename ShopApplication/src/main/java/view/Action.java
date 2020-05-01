package view;

public abstract class Action {
    private String actionPattern;
    private String actionCommand;
    private String name;

    public Action(String name, String actionPattern, String actionCommand) {
        this.name = name;
        this.actionPattern = actionPattern;
        this.actionCommand = actionCommand;
    }

    public String getActionPattern() {
        return actionPattern;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(String command);
}
