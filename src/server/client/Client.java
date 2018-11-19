package server.client;

import common.Coordinate;
import server.Map;
import server.movingObjects.Snake;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {
    private ClientHeartBeatHandler clientHeartBeatHandler;
    private ClientConnectionHandler clientConnectionHandler;
    private int clientNumber;
    private Snake snake;
    Client(ClientHeartBeatHandler clientHeartBeatHandler, ClientConnectionHandler clientConnectionHandler, int clientNumber) {
        this.clientHeartBeatHandler = clientHeartBeatHandler;
        this.clientConnectionHandler = clientConnectionHandler;
        this.clientNumber = clientNumber;
        snake = new Snake(Map.getInstance().getMapSize(), this);

        switch (clientNumber) {
            case 1:
                sendMapData();
                sendFirstSnakesData();
                break;
            case 2:
                send90DegreeRotatedMapData();
                sendFirstSnakesData90DegreeRotated();
                break;
            case 3:
                send180DegreeRotatedMapData();
                sendFirstSnakesData180DegreeRotated();
                break;
            case 4:
                send270DegreeRotatedMapData();
                sendFirstSnakesData270DegreeRotated();
                break;
        }
    }

    private String getDirection() {
        return clientConnectionHandler.getDirection();
    }

    public int getClientNumber() {
        return clientNumber;
    }

    private void sendMapData() {
        clientConnectionHandler.sendDataToClient(Map.getInstance().getJSONObjectFormat());
    }
    private void send90DegreeRotatedMapData() {
        clientConnectionHandler.sendDataToClient(Map.getInstance().get90DegreeJSONObjectFormat());
        System.out.println("=====----> " + Map.getInstance().get90DegreeJSONObjectFormat());
    }
    private void send180DegreeRotatedMapData() {
        clientConnectionHandler.sendDataToClient(Map.getInstance().get180DegreeJSONObjectFormat());
    }
    private void send270DegreeRotatedMapData() {
        clientConnectionHandler.sendDataToClient(Map.getInstance().get270DegreeJSONObjectFormat());
    }


    private void sendFirstSnakesData() {
        Clients.getInstance().getClient1Data(new LinkedList<>());
    }
    private void sendFirstSnakesData90DegreeRotated() {
        Clients.getInstance().getClient2Data(new LinkedList<>());
    }
    private void sendFirstSnakesData180DegreeRotated() {
        Clients.getInstance().getClient3Data(new LinkedList<>());
    }
    private void sendFirstSnakesData270DegreeRotated() {
        Clients.getInstance().getClient4Data(new LinkedList<>());
    }

    void sendSnakeData(JSONObject data) {
        clientConnectionHandler.sendDataToClient(data);
    }
    void move() throws Snake.SnakeNotAllowedMoveException {
        snake.move(getDirection());
    }
    Coordinate getHead() {
        return snake.getHead();
    }
    Coordinate removeTail() {
        return snake.removeTail();
    }

    JSONArray getSnakeJSONFormat() {
        return snake.getJSONArrayFormat();
    }

    List<Coordinate> destroy() {
        // TODO: kill both threads!!! ???
        return snake.removeSnake();
    }

    List<Coordinate> getSnakeArray() {
        return snake.getSnakeArray();
    }
}
