package view;

import java.util.ArrayList;
import java.util.Arrays;

public class Form {
    private static String[] fields;
    private static String[] fieldRegex;
    private static String[] results;

    public static int createForm(String[] fields, String[] fieldRegex) {
        Form.fields = fields.clone();
        Form.fieldRegex = fieldRegex.clone();
        Form.results = results.clone();
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
                if (inputField(currIndex + 1) == 0 || currIndex == fields.length - 1) {
                    return 0;
                }
            } else {
                System.out.println("invalid entry.");
            }
        }
    }

    public static String[] getResults() {
        fields = null;
        fieldRegex = null;
        String[] tmp = Arrays.copyOf(results, results.length);
        results = null;
        return tmp;
    }
}
