package view.Actions;

import view.Action;
import view.Constants;
import view.Menu;

public class Back extends Action {
    private Menu parent;

    Back(Menu parent) {
        super(Constants.Actions.backPattern, Constants.Actions.backCommand);
        this.parent = parent;
    }

    public void setParent(Menu newParent) {
        this.parent = newParent;
    }

    @Override
    public void execute(String command) {
        printSeparator();
        parent.run();
    }

}