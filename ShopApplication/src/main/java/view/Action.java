package view;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Action {
    private String actionPattern;
    private String actionCommand;
    private String name;
    private static Stack<Action> stackTrace;

    public Action(String name, String actionPattern, String actionCommand) {
        this.name = name;
        this.actionPattern = actionPattern;
        this.actionCommand = actionCommand;
        this.stackTrace = new Stack<>();
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

    protected Matcher getMatcherReady(String command) {
         Matcher matcher = Pattern.compile(this.actionPattern).matcher(command);
         matcher.find();
         return matcher;
    }

    public void run(String command) {
        stackTrace.push(this);
        this.execute(command);
    }

    public abstract void execute(String command);
}
