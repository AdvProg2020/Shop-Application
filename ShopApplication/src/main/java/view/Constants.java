package view;

public class Constants {
    private static final String caseInsensitiveMode = "(?i)";
    private static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";
    //private static final String
    //private static final String

    public static class MenuCommandAndPattern {
        public static final String accountMenuCommand = "account menu";
        public static final String accountMenuPattern = caseInsensitiveMode + accountMenuCommand;
        public static final String allProductsMenuCommand = "products menu";
        public static final String allProductsMenuPattern = caseInsensitiveMode + allProductsMenuCommand;
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
//        public static final String
    }

    public static class ActionCommandAndPattern {
        public static final String backCommand = "back";
        public static final String backPattern = caseInsensitiveMode + backCommand;
        public static final String exitCommand = "exit";
        public static final String exitPattern = caseInsensitiveMode + exitCommand;
        public static final String loginCommand = "login [username]";
        public static final String loginPattern = caseInsensitiveMode + "^login" + spacePattern + argumentPattern + "$";
        public static final String registerCommand = "create account [type] [username]";
        public static final String registerPattern =
                caseInsensitiveMode + "^create account" + spacePattern + argumentPattern + spacePattern + argumentPattern + "$";
        //these Actions are for AllProductsMenu.
        public static final String showProductsCommand = "show products";
        public static final String showProductsPattern = caseInsensitiveMode + showProductsCommand;
        public static final String showCategoriesCommand = "show categories [-all]";
        public static final String showCategoriesPattern = caseInsensitiveMode + "^show categories" + spacePattern + "(-all)?$";
//        public static final String
//        public static final String
    }
}
