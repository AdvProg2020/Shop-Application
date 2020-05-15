package view;

import java.util.ArrayList;

public class Form {
    private String[] fields;
    private String[] fieldRegex;
    private String[] results;
    private String arrayListRegex;
    private String arrayListField;
    private ArrayList<String> listResult;

    Form(String[] fields, String[] fieldRegex) {
        this.fields = fields.clone();
        this.fieldRegex = fieldRegex.clone();
        this.results = new String[fields.length];
    }

    public void setupArrayForm(String arrayListField, String arrayListRegex) {
        this.arrayListField = arrayListField;
        this.arrayListRegex = arrayListRegex;
        listResult = new ArrayList<>();
    }

    public int takeInput() {
        return inputField(0);
    }

    private int inputField(int currIndex) {
        while(true) {
            System.out.println(fields[currIndex] + ":");
            String response = View.getNextLineTrimmed();
            if (response.equalsIgnoreCase("back")) {
                return -1;
            } else if (response.matches(fieldRegex[currIndex])) {
                results[currIndex] = response;
                if (currIndex == fields.length -1) {
                    if (inputList() == -1) {continue;}
                    else {return 0;}
                } else if (inputField(currIndex + 1) == 0) {return 0;}
                else {continue;}
            } else {
                System.out.println("invalid entry.");
            }
        }
    }

    private int inputList() {
        while(true) {
            System.out.print("enter " + arrayListField + " (enter \"back\" to go back or \"exit\" to exit):\n" + (listResult.size() + 1) + ". ");
            String input = View.getNextLineTrimmed();
            if (input.equalsIgnoreCase("back")) {
                if (listResult.size() == 0) {return -1;}
                else {
                    listResult.remove(listResult.size() - 1);
                    continue;
                }
            } else if (input.equalsIgnoreCase("exit")){
                return 0;
            } else {
                listResult.add(input);
            }
        }
    }

    public ArrayList<String> getListResult() {
        return listResult;
    }

    public String[] getResults() {
        return results;
    }
}
