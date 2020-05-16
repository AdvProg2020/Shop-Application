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

    //TODO: get field names
    protected void printList(ArrayList<String[]> list) {
        if (list.isEmpty()) {
            System.out.println("this list is empty!");
            return;
        }
        int args = list.get(0).length;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            System.out.print((i + 1) + ".");
            for (int j = 0; j < args; j++) {
                System.out.print( " " + list.get(i)[j]);
            }
            System.out.print("\n");
        }
    }

    protected void printSeparator() {
        System.out.println("-------------------------------\n");
    }

    protected String getGroup(String command, int groupIndex) {
        Matcher commandMatcher = getMatcherReady(command);
        return commandMatcher.group(groupIndex);
    }



    public abstract void execute(String command);

}
