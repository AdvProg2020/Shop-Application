package Client.HollowController;

import Client.view.Constants;
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
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.supporterGetActiveChats, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new Exceptions.UnAuthorizedAccountException();
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public ArrayList<String[]> getArchiveChats() throws Exceptions.UnAuthorizedAccountException {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.supporterGetArchiveChats, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new Exceptions.UnAuthorizedAccountException();
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public ArrayList<String[]> viewChat(String chatId) throws Exceptions.InvalidChatIdException {
        String body = convertToJson(chatId);
        String response = sender.sendRequest(Constants.Commands.supporterViewChat, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new Exceptions.InvalidChatIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void sendMessage(String chatId, String text) throws Exceptions.InvalidChatIdException {
        String body = convertToJson(chatId, text);
        String response = sender.sendRequest(Constants.Commands.supporterSendMessage, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new Exceptions.InvalidChatIdException(nameBody[1]);
        }
    }

    public void deleteChat(String chatId) throws Exceptions.InvalidChatIdException {
        String body = convertToJson(chatId);
        String response = sender.sendRequest(Constants.Commands.supporterDeleteChat, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new Exceptions.InvalidChatIdException(nameBody[1]);
        }
    }

    public String[] viewChatById(String chatId) throws Exceptions.InvalidChatIdException {
        String body = convertToJson(chatId);
        String response = sender.sendRequest(Constants.Commands.supporterViewChatById, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new Exceptions.InvalidChatIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }
}
