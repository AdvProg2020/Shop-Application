package view.GUI;

public final class Constants {
    public static final String caseInsensitiveMode = "(?i)";
    public static final String argumentPattern = "(\\S+)";
    private static final String spacePattern = "\\s+";
    public static final String unsignedIntPattern = "\\+?(\\d+)";
    public static final String usernamePattern = "(\\w+)";
    public static final String doublePattern = "(\\d+(?:\\.\\d+)?)";
    public static final String percentagePattern = "%?" + doublePattern + "%?";
    public static final String datePattern = "\\d{2}[/-]\\d{2}[/-]\\d{2}";
    public static final String IRLNamePattern = "[a-zA-Z]+( [a-zA-Z]+)*";
    public static final String emailPattern = "^.+@.+\\.com$";
    public static final String phonePattern = "\\+?\\d{8,12}";
    public static final String anonymousUserType = "Anonymous";
    public static final String adminUserType = "Admin";
    public static final String customerUserType = "Customer";
    public static final String sellerUserType = "Seller";
    public static final String SUPER_CATEGORY_NAME = "SuperCategory";
    public static final String[] types = new String[]{anonymousUserType, customerUserType, sellerUserType, adminUserType};

    public static int getTypeByIndex(String type) {
        for (int i = 0; i < types.length; i++) {
            if (type.equalsIgnoreCase(types[i])) {
                return i;
            }
        }
        return -1;
    }

    public static class FXMLs {
        public static final String mainMenu = "MainMenu";
        public static final String base = "Base";
        public static final String accountMenu = "AccountMenu";
        public static final String shoppingCartMenu = "ShoppingCartMenu";
        public static final String loginPopUp = "LoginPopUp";
        public static final String registerPopUp = "RegisterPopUp";
        public static final String personalInfoMenu = "PersonalInfoMenuCopy";
    }
}