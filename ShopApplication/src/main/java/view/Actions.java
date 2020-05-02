package view;

import java.util.ArrayList;

public class Actions {
    public static class BackAction extends Action {
        private Menu parent;
        BackAction(String name, Menu parent) {
            super(name, Constants.ActionCommandAndPattern.backPattern, Constants.ActionCommandAndPattern.backCommand);
            this.parent = parent;
        }

        @Override
        public void execute(String command) {
            parent.run();
        }
    }

    public static class ExitAction extends Action {
        ExitAction(String name) {
            super(name, Constants.ActionCommandAndPattern.exitPattern, Constants.ActionCommandAndPattern.exitCommand);
        }

        @Override
        public void execute(String command) {
            System.exit(1);
        }
    }

    public static  class LoginAction extends Action {
        LoginAction(String name) {
            super(name, Constants.ActionCommandAndPattern.loginPattern, Constants.ActionCommandAndPattern.loginCommand);
        }

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class RegisterAction extends Action {
        RegisterAction(String name) {
            super(name, Constants.ActionCommandAndPattern.registerPattern, Constants.ActionCommandAndPattern.registerCommand);
        }

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowProductsAction extends Action {
        private ArrayList<String> categoryTree;
        ShowProductsAction(String name, ArrayList<String> categoryTree) {
            super(name, Constants.ActionCommandAndPattern.showProductsPattern, Constants.ActionCommandAndPattern.showProductsCommand);
            this.categoryTree = categoryTree;
        }

        //TODO: implement.
        @Override
        public void execute(String command) {

        }
    }

    public static class ShowCategories extends Action {
        private ArrayList<String> categoryTree;
        ShowCategories(String name, ArrayList<String> categoryTree) {
            super(name, Constants.ActionCommandAndPattern.showCategoriesPattern, Constants.ActionCommandAndPattern.showCategoriesCommand);
        }

        //TODO: imp.
        @Override
        public void execute(String command) {

        }
    }
}
