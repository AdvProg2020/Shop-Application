package view;

import java.util.ArrayList;
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

    protected static Action getPreviousAction() {
        Action curAction = stackTrace.pop();
        Action prevAction = stackTrace.peek();
        stackTrace.push(curAction);
        return prevAction;
    }

    public void run(String command) {
        stackTrace.push(this);
        this.execute(command);
    }

    protected void printList(ArrayList<String[]> list, int args) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            System.out.println(i + ".");
            for (int j = 0; j < args; j++) {
                System.out.print( " " + list.get(i)[j]);
            }
        }
    }

    public abstract void execute(String command);
}
