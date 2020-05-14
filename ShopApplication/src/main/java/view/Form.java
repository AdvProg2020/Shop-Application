package view;

public class Form {
    private static String[] fields;
    private static String[] fieldRegex;
    private static String[] results;

    public static int createForm(String[] fields, String[] fieldRegex, String[] results) {
        Form.fields = fields;
        Form.fieldRegex = fieldRegex;
        Form.results = results;
        return inputField(0);
    }

    private static int inputField(int currIndex) {
        while(true) {
            System.out.println(fields[currIndex] + ": ");
            String response = View.getNextLineTrimmed();
            if (response.equalsIgnoreCase("back")) {
                return -1;
            } else if (response.matches(fieldRegex[currIndex])) {
                results[currIndex] = response;
                if (inputField(currIndex + 1) == 0) {
                    return 0;
                } else {continue;}
            } else {
                System.out.println("invalid entry.");
                continue;
            }
        }
    }
}
