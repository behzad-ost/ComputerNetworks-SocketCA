package server.client;

import common.Coordinate;
import server.Map;
import server.Move;
import server.ServerConfig;
import server.movingObjects.Food;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONObject;
import server.movingObjects.Snake;

public class Clients {
    private static Clients clients = new Clients();
    private Clients() {}
    public static Clients getInstance() {
        return clients;
    }

    private Client client1 = null;
    private Client client2 = null;
    private Client client3 = null;
    private Client client4 = null;

    public Client addClient(ClientHeartBeatHandler clientHeartBeatHandler, ClientConnectionHandler clientConnectionHandler) throws Exception {
        if (client1 == null) {
            client1 = new Client(clientHeartBeatHandler, clientConnectionHandler, 1);
            Timer timer = new Timer();
            timer.schedule(new Move(), 0, ServerConfig.GameConfig.snakeSpeed_milPerSec);
            return client1;
        } else if (client2 == null) {
            client2 = new Client(clientHeartBeatHandler, clientConnectionHandler, 2);
            return client2;
        } else if (client3 == null) {
            client3 = new Client(clientHeartBeatHandler, clientConnectionHandler, 3);
            return client3;
        } else if(client4 == null) {
            client4 = new Client(clientHeartBeatHandler, clientConnectionHandler, 4);
            return client4;
        } else
            throw new Exception();
    }

    public List<Coordinate> moveAllClients() {
        List<Coordinate> removedElements = new LinkedList<>();

        removedElements.addAll(moveClient(client1));
        removedElements.addAll(moveClient(client2));
        removedElements.addAll(moveClient(client3));
        removedElements.addAll(moveClient(client4));

        removedElements.addAll(checkForCrash(client1, client2, client3, client4));

        if (client1 != null && Food.getInstance().getFoodPosition().equals(client1.getHead())) {
            Food.getInstance().generateFood();
            removedElements.add(client1.removeTail());
        } else if (client2 != null && Food.getInstance().getFoodPosition().equals(client2.getHead())) {
            Food.getInstance().generateFood();
            removedElements.add(client2.removeTail());
        } else if (client3 != null && Food.getInstance().getFoodPosition().equals(client3.getHead())) {
            Food.getInstance().generateFood();
            removedElements.add(client3.removeTail());
        } else if (client4 != null && Food.getInstance().getFoodPosition().equals(client4.getHead())) {
            Food.getInstance().generateFood();
            removedElements.add(client4.removeTail());
        } else {
            if (client1 != null)
                removedElements.add(client1.removeTail());
            if (client2 != null)
                removedElements.add(client2.removeTail());
            if (client3 != null)
                removedElements.add(client3.removeTail());
            if (client4 != null)
                removedElements.add(client4.removeTail());
        }
        return removedElements;
    }

    private List<Coordinate> checkForCrash(Client client1, Client client2, Client client3, Client client4) {
        List<Coordinate> removeSnake = new LinkedList<>();
        if(checkCrash(client1, client2, client3, client4))
            removeSnake.addAll(client1.destroy());
        if(checkCrash(client2, client1, client3, client4))
            removeSnake.addAll(client2.destroy());
        if(checkCrash(client3, client2, client1, client4))
            removeSnake.addAll(client3.destroy());
        if(checkCrash(client4, client2, client3, client1))
            removeSnake.addAll(client4.destroy());
        return removeSnake;
    }

    private boolean checkCrash(Client client1, Client client2, Client client3, Client client4) {
        if(client1 == null)
            return false;
        if(checkSelfCrash(client1))
            return true;
        Coordinate head = client1.getHead();
        if(client2 != null)
            return checkSnakeHead(head,client2);
        if(client3 != null)
            return checkSnakeHead(head,client3);
        if(client4 != null)
            return checkSnakeHead(head,client4);
        return false;
    }

    private boolean checkSelfCrash(Client client) {
        Coordinate head = client.getHead();
        List<Coordinate> snake = client.getSnakeArray();
        for(int i = 1 ; i < snake.size() ; i++){
            if(head.equals(snake.get(i))){
                return true;
            }
        }
        return false;
    }

    private boolean checkSnakeHead(Coordinate head, Client client) {
        List<Coordinate> snake = client.getSnakeArray();
        for(int i = 0 ; i < snake.size() ; i++){
            if(head.equals(snake.get(i))){
                return true;
            }
        }
        return false;
    }

    private List<Coordinate> moveClient(Client client) {
        List<Coordinate> removeSnake = new LinkedList<>();
        if (client != null) {
            try {
                client.move();
            } catch (Snake.SnakeNotAllowedMoveException notAllowedMoveException) {
                removeSnake.addAll(client.destroy());
            }
        }
        return removeSnake;
    }

    JSONObject getClient1Data(List<Coordinate> removedElements) {
        JSONObject movingObjectsData = new JSONObject();

        JSONArray snakes = new JSONArray();
        if (client1 != null)
            snakes.put(client1.getSnakeJSONFormat());
        if (client2 != null)
            snakes.put(client2.getSnakeJSONFormat());
        if (client3 != null)
            snakes.put(client3.getSnakeJSONFormat());
        if (client4 != null)
            snakes.put(client4.getSnakeJSONFormat());

        JSONArray remove = new JSONArray();
        for (Coordinate removedElement: removedElements)
            remove.put(removedElement.getJSONArrayFormat());

        movingObjectsData.put("food", Food.getInstance().getJSONArrayFormat());
        movingObjectsData.put("snakes", snakes);
        movingObjectsData.put("remove", remove);
        return movingObjectsData;
    }

    JSONObject getClient2Data(List<Coordinate> removedElements) {
        JSONObject movingObjectsData = new JSONObject();

        JSONArray snakes = new JSONArray();
        JSONArray snake1 = new JSONArray();
        JSONArray snake2 = new JSONArray();
        JSONArray snake3 = new JSONArray();
        JSONArray snake4 = new JSONArray();
        if (client1 != null) {
            List<Coordinate> rotated = Map.rotate90Degree(client1.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake1.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake1);
        if (client2 != null) {
            List<Coordinate> rotated = Map.rotate90Degree(client2.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake2.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake2);
        if (client3 != null) {
            List<Coordinate> rotated = Map.rotate90Degree(client3.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake3.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake3);
        if (client4 != null) {
            List<Coordinate> rotated = Map.rotate90Degree(client4.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake4.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake4);

        JSONArray remove = new JSONArray();
        for (Coordinate removedElement: removedElements)
            remove.put(Map.rotate90Degree(removedElement).getJSONArrayFormat());

        movingObjectsData.put("food", Map.rotate90Degree(Food.getInstance().getFoodPosition()).getJSONArrayFormat());
        movingObjectsData.put("snakes", snakes);
        movingObjectsData.put("remove", remove);
        return movingObjectsData;
    }

    JSONObject getClient3Data(List<Coordinate> removedElements) {
        JSONObject movingObjectsData = new JSONObject();

        JSONArray snakes = new JSONArray();
        JSONArray snake1 = new JSONArray();
        JSONArray snake2 = new JSONArray();
        JSONArray snake3 = new JSONArray();
        JSONArray snake4 = new JSONArray();
        if (client1 != null) {
            List<Coordinate> rotated = Map.rotate180Degree(client1.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake1.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake1);
        if (client2 != null) {
            List<Coordinate> rotated = Map.rotate180Degree(client2.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake2.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake2);
        if (client3 != null) {
            List<Coordinate> rotated = Map.rotate180Degree(client3.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake3.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake3);
        if (client4 != null) {
            List<Coordinate> rotated = Map.rotate180Degree(client4.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake4.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake4);

        JSONArray remove = new JSONArray();
        for (Coordinate removedElement: removedElements)
            remove.put(Map.rotate180Degree(removedElement).getJSONArrayFormat());

        movingObjectsData.put("food", Map.rotate180Degree(Food.getInstance().getFoodPosition()).getJSONArrayFormat());
        movingObjectsData.put("snakes", snakes);
        movingObjectsData.put("remove", remove);
        return movingObjectsData;
    }

    JSONObject getClient4Data(List<Coordinate> removedElements) {
        JSONObject movingObjectsData = new JSONObject();

        JSONArray snakes = new JSONArray();
        JSONArray snake1 = new JSONArray();
        JSONArray snake2 = new JSONArray();
        JSONArray snake3 = new JSONArray();
        JSONArray snake4 = new JSONArray();
        if (client1 != null) {
            List<Coordinate> rotated = Map.rotate270Degree(client1.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake1.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake1);
        if (client2 != null) {
            List<Coordinate> rotated = Map.rotate270Degree(client2.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake2.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake2);
        if (client3 != null) {
            List<Coordinate> rotated = Map.rotate270Degree(client3.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake3.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake3);
        if (client4 != null) {
            List<Coordinate> rotated = Map.rotate270Degree(client4.getSnakeArray());
            for (Coordinate coordinate: rotated)
                snake4.put(coordinate.getJSONArrayFormat());
        }
        snakes.put(snake4);

        JSONArray remove = new JSONArray();
        for (Coordinate removedElement: removedElements)
            remove.put(Map.rotate270Degree(removedElement).getJSONArrayFormat());

        movingObjectsData.put("food", Map.rotate270Degree(Food.getInstance().getFoodPosition()).getJSONArrayFormat());
        movingObjectsData.put("snakes", snakes);
        movingObjectsData.put("remove", remove);
        return movingObjectsData;
    }

    public void sendNewDataToAllClients(List<Coordinate> removedElements) {
        if (client1 != null)
            client1.sendSnakeData(getClient1Data(removedElements));
        if (client2 != null)
            client2.sendSnakeData(getClient2Data(removedElements));
        if (client3 != null)
            client3.sendSnakeData(getClient3Data(removedElements));
        if (client4 != null)
            client4.sendSnakeData(getClient4Data(removedElements));
    }
}
