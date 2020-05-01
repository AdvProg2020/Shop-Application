package view;

import java.util.*;

public abstract class Menu {
    protected String name;
    protected boolean isAccountMenuAccessible;
    protected static Menu accountMenu;
    protected Menu parent;
    protected Map<Integer, Menu> subMenus;
    protected Map<Integer, Action> subActions;
    static protected ArrayList<Menu>  allMenus;
    static protected Scanner sc;
    protected String commandList;
    protected String commandPattern;


    static {
        sc = new Scanner(System.in);
        allMenus = new ArrayList<Menu>();
    }


    public Menu(String name, boolean isAccountMenuAccessible, Menu parent, String commandPattern, String commandList) {
        this.name = name;
        this.isAccountMenuAccessible = isAccountMenuAccessible;
        this.parent = parent;
        this.commandPattern = commandPattern;
        this.commandList = commandList;
        allMenus.add(this);
        subMenus = new HashMap<>();
        subActions = new HashMap<>();
        initSubMenus();
        initSubActions();
    }

    protected abstract void initSubMenus();
    protected abstract void initSubActions();

    static protected String getNextLineTrimmed() {
        return sc.nextLine().trim();
    }

    public String getName() {
        return name;
    }

    public String getCommandPattern() {
        return commandPattern;
    }

    public String getCommandList() {
        return commandList;
    }

    public void showCommandList() {
        System.out.println(commandList);
    }

    static protected <T> void printArray(ArrayList<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }

    protected void run() {
        this.show();
        this.execute();
    }

    //TODO: redo show
    public void show() {
        if () {

        }
        for (Integer menuIndex : new TreeSet<Integer>(subMenus.keySet())) {
            System.out.println(menuIndex + ". " + subMenus.get(menuIndex));
        }
        int index = subMenus.keySet().size();

        if (isAccountMenuAccessible) {
            System.out.println(++index  + ". " + accountMenu);
        }
        if (this.parent == null) {
            System.out.println(++index + ". exit");
        } else {
            System.out.println(++index + ". back");
        }
    }

    public boolean shouldGoBack(String command) {
        if (command.equalsIgnoreCase("back") || command.equalsIgnoreCase("exit") ||
            command.equalsIgnoreCase(Integer.toString(subMenus.keySet().size() + ((isAccountMenuAccessible) ? 2 : 1)))) {
            return true;
        } else {
            return false;
        }
    }

    public int getMenuIndex(Menu menu) throws Exception{
        for (Integer index : subMenus.keySet()) {
            if (subMenus.get(index).equals(menu)) {
                return index;
            }
        }
        throw new Exception("Not in the sub-menus");
    }

    public void execute() {
        String command = getNextLineTrimmed();

    }

    @Override
    public String toString() {
        return this.getName();
    }
}
