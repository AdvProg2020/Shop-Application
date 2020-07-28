package Client.HollowController;

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

    public  String sendRequest(String command, String body) {
        try {
             socket = new Socket("localhost", 53514);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            String request = command;
            request += " " + ((authToken == null)? "0": authToken) + " ";
            if ( ! body.equals("")) request += body;

            sendMessage(request, dos);

            System.out.println("client: " + authToken + " sent: " + request);

            String response = receiveMessage(dis);

            dos.writeUTF("ok");
            dos.flush();
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

    private void sendMessage(String request, DataOutputStream dos) {
        int size = 64000;
        int parts = request.getBytes().length/size + 1;
        request = parts + " " + request;

        try {
            for (int i = 0; i < parts; i++) {
                dos.writeUTF(request.substring(i * size, Math.min((i + 1) * size, request.length())));
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage(DataInputStream dis) {
        try {
            StringBuilder response = new StringBuilder("");
            String firstPart = dis.readUTF();
            String num = firstPart.split(" ")[0];
            response.append(firstPart.substring(num.length() + 1));
            int parts = Integer.parseInt(num) - 1;
            for (int i = 0; i < parts; i++) {
                response.append(dis.readUTF());
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
