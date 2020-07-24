package Client.HollowController;

import Client.view.Constants;

import java.io.*;
import java.net.Socket;

public class Sender {
    private static Sender instance;
    private static int serverPort;
    private static String authToken;
    private Socket socket;

    public static Sender getInstance() {
        if (instance != null) return instance;
        else return instance = new Sender();
    }

    public String sendRequest(String command, String body) {
        try {
            socket = new Socket("localhost", 51153);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            String request = command;
            request += " " + ((authToken == null)? "0": authToken) + " ";
            if ( ! body.equals("")) request += body;
            dos.writeUTF(request);
            dos.flush();
            System.out.println("client: " + authToken + " sent: " + request);

            String response = dis.readUTF();
            if (response.startsWith("authentication token:")) {
                setAuthToken(response);
                return "authentication token set successfully";
            } else return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setAuthToken(String response) {
        authToken = response.substring(response.indexOf(":") + 1);
    }
}
