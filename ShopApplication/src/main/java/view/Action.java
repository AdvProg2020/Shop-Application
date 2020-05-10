package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Action {
    protected String actionPattern;
    protected String actionCommand;
    protected String name;

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

    protected Matcher getMatcherReady(String command) {
         Matcher matcher = Pattern.compile(this.actionPattern).matcher(command);
         matcher.find();
         return matcher;
    }

    public abstract void execute(String command);
}
