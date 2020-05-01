package view;

public class Actions {
    public static class BackAction extends Action {
        private Menu parent;
        BackAction(String name, String actionPattern, Menu parent) {
            super(name, actionPattern);
            this.parent = parent;
        }

        @Override
        public void execute(String command) {
            parent.run();
        }
    }

    public static class ExitAction extends Action {
        ExitAction(String name, String actionPattern) {
            super(name, actionPattern);
        }

        @Override
        public void execute(String command) {
            System.exit(1);
        }
    }
}
