package view;

public abstract class Action {
    private String actionPattern;
    private String name;

    public Action(String name, String actionPattern) {
        this.name = name;
        this.actionPattern = actionPattern;
    }

    public String getActionPattern() {
        return actionPattern;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(String command);
}
