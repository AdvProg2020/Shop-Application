package Server.ServerGate;

import Server.ServerInitializer;
import Server.controller.*;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ServerRequestHandler extends Thread {
    static HashMap<String, Session> sessions = new HashMap<>();
    static int counter = 0;
    Socket clientSocket;
    String message;
    String command = null;
    String body;
    String authToken;
    DataInputStream dis;
    DataOutputStream dos;

    static class Session {
        Controller mainController;
        AdminController adminController;
        SellerController sellerController;
        CustomerController customerController;
        SupporterController supporterController;
        long lastReq;

        public Session() {
            mainController = new Controller(ServerInitializer.databaseManager);
            sellerController = new SellerController(mainController);
            customerController = new CustomerController(mainController);
            adminController = new AdminController(mainController);
            supporterController = new SupporterController(mainController);
            lastReq = System.currentTimeMillis() / 1000;
        }

        public SupporterController getSupporterController() {
            return supporterController;
        }

        public Controller getMainController() {
            return mainController;
        }

        public AdminController getAdminController() {
            return adminController;
        }

        public SellerController getSellerController() {
            return sellerController;
        }

        public CustomerController getCustomerController() {
            return customerController;
        }

        public void updateLastRequest() {
            lastReq = System.currentTimeMillis() / 1000;
        }

        public long getTimePassedInMinutes() {
            return getTimePassedInSeconds() / 60;
        }

        public long getTimePassedInSeconds() {
            return System.currentTimeMillis() / 1000 - lastReq;
        }
    }

    public ServerRequestHandler(Socket clientSocket) throws IOException {
        super();
        this.clientSocket = clientSocket;
        dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            message = receiveMessage(dis);
            System.out.println("Main Server received a request from " + clientSocket.getInetAddress() + "\ncontent: " + message);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        parseMessage();
        if (command == null) {
            try {
                sendMessage("speak persian", dos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return;
            }
        }

        if (command.equals(Commands.authTokenRequest)) {
            sayHi();
        } else {
            try {
                Session s = sessions.getOrDefault(authToken, null);
                if (s == null) {
                    sendMessage("invalid auth token", dos);
//                } else if (s.getTimePassedInMinutes() > 1) {
//                    dos.writeUTF();
                } else {
                    String returnValue = Commands.allTasks.get(command).execute(s, body.split("\\*"));
                    s.updateLastRequest();
                    sendMessage(returnValue, dos);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String certification = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String request, DataOutputStream dos) throws IOException {
        int size = 64000;
        int parts = request.getBytes().length / size + 1;
        request = parts + " " + request;

        for (int i = 0; i < parts; i++) {
            dos.writeUTF(request.substring(i * size, Math.min((i + 1) * size, request.length())));
            dos.flush();
        }
    }

    private String receiveMessage(DataInputStream dis) throws IOException {
        StringBuilder response = new StringBuilder("");
        String firstPart = dis.readUTF();
        String num = firstPart.split(" ")[0];
        response.append(firstPart.substring(num.length() + 1));
        int parts = Integer.parseInt(num) - 1;
        for (int i = 0; i < parts; i++) {
            response.append(dis.readUTF());
        }
        return response.toString();
    }

    private void parseMessage() {
        //checking the regex
        if (!message.matches(Commands.CommandRegex)) return;

        int firstSpaceIndex = message.indexOf(" ");
        int secondSpaceIndex = message.indexOf(" ", firstSpaceIndex + 1);

        String firstPart = message.substring(0, firstSpaceIndex);
        if (!Commands.allTasks.containsKey(firstPart)) return;
        command = firstPart;

        authToken = message.substring(firstSpaceIndex + 1, secondSpaceIndex);

        body = message.substring(secondSpaceIndex + 1);
    }

    private void sayHi() {
        //create a token. create a session and put it in hash map and return the token.
        String token = Integer.toString(++counter);
        sessions.put(token, new Session());
        try {
            sendMessage("authentication token:" + token, dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Session> getSessions() {
        return sessions;
    }
}
