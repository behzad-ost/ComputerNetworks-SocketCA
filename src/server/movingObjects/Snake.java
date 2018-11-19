package server.movingObjects;

import common.Coordinate;
import server.Map;
import server.client.Client;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

public class Snake {
    private LinkedList<Coordinate> snakeArray;
    private Client client;
    public Snake(int mapSize, Client client) {
        snakeArray = new LinkedList<>();
        this.client = client;

        switch (client.getClientNumber()) {
            case 1:
                snakeArray.add(new Coordinate(0, 2));
                snakeArray.add(new Coordinate(0, 1));
                snakeArray.add(new Coordinate(0, 0));
                break;
            case 2:
                snakeArray.add(new Coordinate(2, mapSize - 1));
                snakeArray.add(new Coordinate(1, mapSize - 1));
                snakeArray.add(new Coordinate(0, mapSize - 1));
                break;
            case 3:
                snakeArray.add(new Coordinate(mapSize - 1, mapSize - 3));
                snakeArray.add(new Coordinate(mapSize - 1, mapSize - 2));
                snakeArray.add(new Coordinate(mapSize - 1, mapSize - 1));
                break;
            case 4:
                snakeArray.add(new Coordinate(mapSize - 3, 0));
                snakeArray.add(new Coordinate(mapSize - 2, 0));
                snakeArray.add(new Coordinate(mapSize - 1, 0));
                break;
        }
    }

    public void move(String direction) throws SnakeNotAllowedMoveException {
        if (snakeArray.size() == 0)
            return;

        Coordinate head = snakeArray.getFirst();
        Coordinate newHead = new Coordinate(head);

        switch (direction) {
            case "right":
                newHead.setY(newHead.getY() + 1);
                break;
            case "left":
                newHead.setY(newHead.getY() - 1);
                break;
            case "up":
                newHead.setX(newHead.getX() - 1);
                break;
            case "down":
                newHead.setX(newHead.getX() + 1);
                break;
        }

        if (!Map.getInstance().checkForObstacle(newHead) || !Map.getInstance().checkForBound(newHead))
            throw new SnakeNotAllowedMoveException("Wall or obstacle destroyed me :(");

        snakeArray.addFirst(newHead);
    }

    public Coordinate getHead() {
        if (snakeArray.size() == 0)
            return new Coordinate(-1, -1);
        else
            return snakeArray.getFirst();
    }

    public Coordinate removeTail() {
        if (snakeArray.size() == 0)
            return new Coordinate(-1, -1);
        else
            return snakeArray.removeLast();
    }

    public List<Coordinate> removeSnake() {
        List<Coordinate> snakeList = new LinkedList<>(snakeArray);
        snakeArray = new LinkedList<>();
        return snakeList;
    }

    public JSONArray getJSONArrayFormat() {
        JSONArray snake = new JSONArray();
        System.out.println(snakeArray);
        for (Coordinate coordinate: snakeArray) {
            snake.put(coordinate.getJSONArrayFormat());
        }
        return snake;
    }

    public class SnakeNotAllowedMoveException extends Throwable {
        SnakeNotAllowedMoveException(String s) {
            super(s);
        }
    }

    public LinkedList<Coordinate> getSnakeArray() {
        return snakeArray;
    }
}
