package view;

import controller.AdminController;
import controller.Controller;
import controller.CustomerController;
import controller.SellerController;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Action {
    private String actionPattern;
    private String actionCommand;

    protected static Controller mainController;
    protected static AdminController adminController;
    protected static CustomerController customerController;
    protected static SellerController sellerController;

    protected Action( String actionPattern, String actionCommand) {
        this.actionPattern = actionPattern;
        this.actionCommand = actionCommand;
    }

    protected String getActionPattern() {
        return actionPattern;
    }

    protected String getActionCommand() {
        return actionCommand;
    }

    protected Matcher getMatcherReady(String command) {
         Matcher matcher = Pattern.compile(this.actionPattern).matcher(command);
         matcher.find();
         return matcher;
    }

    protected void run(String command) {
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
        System.out.println("-------------------------------");
    }

     protected String getGroup(String command, int groupIndex) {
        Matcher commandMatcher = getMatcherReady(command);
        return commandMatcher.group(groupIndex);
    }

     protected int getIndex(String command, ArrayList list) {
        int index = Integer.parseInt(getGroup(command, 1));
        if (index > list.size()) {
            System.out.println("invalid index. please enter a number between 1 and " + list.size());
            return 0;
        }
        return index;
    }

    public abstract void execute(String command);

}
