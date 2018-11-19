package server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHeartBeatHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader clientSendBuffer;
    private PrintWriter clientRecieveBuffer;
    public ClientHeartBeatHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        clientSendBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientRecieveBuffer = new PrintWriter(clientSocket.getOutputStream());
    }
    @Override
    public void run() {
        // get first massage!
        String firstMassage = "";
        try {
            firstMassage = clientSendBuffer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(firstMassage);

        String otherMassages = "";
        while (true) {
            try {
                otherMassages = clientSendBuffer.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(otherMassages);
        }
    }
}
