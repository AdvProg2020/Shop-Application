package view;

public class Constants {
    private static final String caseInsensitiveMode = "(?i)";
    private static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";
    private static final String
    private static final String

    public static class MenuCommandAndPattern {
        public static final String accountMenuCommand = "account menu";
        public static final String accountMenuPattern = caseInsensitiveMode + accountMenuCommand;
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
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
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
        public static final String
    }
}
