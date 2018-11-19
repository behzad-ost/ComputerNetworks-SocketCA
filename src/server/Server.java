package server;

import server.client.ClientConnectionHandler;
import server.client.ClientHeartBeatHandler;
import server.client.Clients;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        int clientNumber = 1;
        ServerSocket serverSocket = new ServerSocket(ServerConfig.serverPort);

        while (true) {
            Socket newClientHeartBeat = serverSocket.accept();
            System.out.println("added new client HeartBeat");
            ClientHeartBeatHandler clientHeartBeatHandler = new ClientHeartBeatHandler(newClientHeartBeat);
            Thread newClientHeartThread = new Thread(clientHeartBeatHandler);
            newClientHeartThread.start();

            Socket newClientDateConnection = serverSocket.accept();
            System.out.println("added new client data socket");
            ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(newClientDateConnection, clientNumber);
            Thread newClientDataThread = new Thread(clientConnectionHandler);
            newClientDataThread.start();
            clientNumber++;
            Clients.getInstance().addClient(clientHeartBeatHandler, clientConnectionHandler);
        }
    }
}
