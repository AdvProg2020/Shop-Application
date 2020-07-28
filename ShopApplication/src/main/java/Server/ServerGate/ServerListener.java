package Server.ServerGate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    ServerSocket serverSocket;
    int port;

    public ServerListener() throws IOException {
        serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
        System.out.println("Server listening on port " + port);
        listen();
    }

    private void listen() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ServerRequestHandler(clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
