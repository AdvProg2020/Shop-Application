package view.GUI;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Action {
    private String actionPattern;
    private String actionCommand;

    public Action(String actionPattern, String actionCommand) {
        this.actionPattern = actionPattern;
        this.actionCommand = actionCommand;
    }

    public String getActionPattern() {
        return actionPattern;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public Matcher getMatcherReady(String command) {
        Matcher matcher = Pattern.compile(this.actionPattern).matcher(command);
        matcher.find();
        return matcher;
    }

    public void run(String command) {
        this.execute(command);
    }

    //TODO: get field names
    public void printList(ArrayList<String[]> list) {
        if (list.isEmpty()) {
            System.out.println("this list is empty!");
            return;
        }
        int args;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            args = list.get(i).length;
            System.out.print((i + 1) + ".");
            for (int j = 0; j < args; j++) {
                System.out.print(" " + list.get(i)[j]);
            }
            System.out.print("\n");
        }
    }

    public void printSeparator() {
        System.out.println("-------------------------------");
    }

    public String getGroup(String command, int groupIndex) {
        Matcher commandMatcher = getMatcherReady(command);
        return commandMatcher.group(groupIndex);
    }

    public int getIndex(String command, ArrayList list) {
        int index = Integer.parseInt(getGroup(command, 1));
        if (index > list.size()) {
            System.out.println("invalid index. please enter a number within the range of indexes shown.");
            return 0;
        }
        return index;
    }

    public abstract void execute(String command);

}
