package view;

public abstract class Action {
    private String actionPattern;
    private String name;

    public Action(String name, String actionPattern) {
        this.name = name;
        this.actionPattern = actionPattern;
    }

    public abstract void execute(String command);
}
