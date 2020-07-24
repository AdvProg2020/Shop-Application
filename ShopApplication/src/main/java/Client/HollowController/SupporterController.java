package Client.HollowController;

import Server.controller.Utilities;
import Server.model.account.Supporter;
import Server.model.chat.Message;
import Server.model.chat.SupportChat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SupporterController {
    private static Type stringArrayType = new TypeToken<String[]>() {
    }.getType();
    private static Type stringListType = new TypeToken<ArrayList<String>>() {
    }.getType();
    private static Type stringArrayListType = new TypeToken<ArrayList<String[]>>() {
    }.getType();

    private Sender sender;

    public SupporterController() {
        sender = Sender.getInstance();
    }

    private String convertToJson(Object... args) {
        StringBuilder array = new StringBuilder();
        Gson gson = new Gson();
        for (int i = 0; i < args.length; i++) {
            array.append(gson.toJson(args[i]));
            if (i != args.length - 1) array.append("*");
        }
        return array.toString();
    }

    private String[] getExceptionNameAndBody(String response) {
        String name = response.substring(response.indexOf(":") + 1, response.indexOf("\n"));
        String body = response.substring(response.indexOf("\n") + 1);
        return new String[]{name, body};
    }

    public ArrayList<String[]> getActiveChats() throws Exceptions.UnAuthorizedAccountException {

    }

    public ArrayList<String[]> getArchiveChats() throws Exceptions.UnAuthorizedAccountException {

    }


    //
    public ArrayList<String[]> viewChat(String chatId) throws Exceptions.InvalidChatIdException {

    }

    public void sendMessage(String chatId, String text) throws Exceptions.InvalidChatIdException {

    }

    public void deleteChat(String chatId) throws Exceptions.InvalidChatIdException {

    }
}
