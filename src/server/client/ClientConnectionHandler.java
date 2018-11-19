package server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class ClientConnectionHandler implements Runnable {
    private Socket clientConnectionSocket;
    private BufferedReader clientSendBuffer;
    private PrintWriter clientReceiveBuffer;
    private String direction = "down";
    int clientNumber;

    public ClientConnectionHandler(Socket clientConnectionSocket, int clientNumber) throws IOException {
        this.clientConnectionSocket = clientConnectionSocket;
        clientSendBuffer = new BufferedReader(new InputStreamReader(clientConnectionSocket.getInputStream()));
        clientReceiveBuffer = new PrintWriter(clientConnectionSocket.getOutputStream());
        this.clientNumber = clientNumber;

        switch (clientNumber) {
            case 1:
                direction = "down";
                break;
            case 2:
                direction = "right";
                break;
            case 3:
                direction = "top";
                break;
            case 4:
                direction = "left";
                break;
        }
    }

    void sendDataToClient(JSONObject data) {
        clientReceiveBuffer.print(data);
        clientReceiveBuffer.flush();
        System.out.println("sent :" + data);
    }

    @Override
    public void run() {
        while (true) {
            String action = "";
            try {
                action = clientSendBuffer.readLine();
                System.out.println(action);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject actionJSON = new JSONObject(action);
            String tmpDirection = actionJSON.getString("action");
            System.out.println(tmpDirection);
//            if (!(direction.equals("up") && tmpDirection.equals("down")
//                    || direction.equals("down") && tmpDirection.equals("up")
//                    || direction.equals("left") && tmpDirection.equals("right")
//                    || direction.equals("right") && tmpDirection.equals("left")))
//                direction = tmpDirection;
            if (clientNumber == 1) {
                direction = tmpDirection;
            } else if (clientNumber == 2) {
                if (tmpDirection.equals("up"))
                    direction = "left";
                else if (tmpDirection.equals("down"))
                    direction = "right";
                else if (tmpDirection.equals("left"))
                    direction = "down";
                else if (tmpDirection.equals("right"))
                    direction = "up";
            } else if (clientNumber == 3) {
                if (tmpDirection.equals("up"))
                    direction = "down";
                else if (tmpDirection.equals("down"))
                    direction = "up";
                else if (tmpDirection.equals("left"))
                    direction = "right";
                else if (tmpDirection.equals("right"))
                    direction = "left";
            } else if (clientNumber == 4) {
                if (tmpDirection.equals("up"))
                    direction = "right";
                else if (tmpDirection.equals("down"))
                    direction = "left";
                else if (tmpDirection.equals("left"))
                    direction = "up";
                else if (tmpDirection.equals("right"))
                    direction = "down   ";
            }

            if (tmpDirection.equals("quit"))
                return;
        }
    }

    String getDirection() {
        return direction;
    }
}
