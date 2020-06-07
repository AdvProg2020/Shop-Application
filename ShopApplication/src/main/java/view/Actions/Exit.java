package view.Actions;

import view.Action;
import view.Constants;

public  class Exit extends Action {
    public Exit() {
        super(Constants.Actions.exitPattern, Constants.Actions.exitCommand);
    }

    @Override
    public void execute(String command) {
        System.exit(1);
    }
}