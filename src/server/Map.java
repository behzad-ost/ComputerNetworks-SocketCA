package server;

import common.Coordinate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;
import server.client.Clients;
import server.movingObjects.Snake;

public class Map {
    private int mapSize;
    private List<Coordinate> obstacles;
    private List<Snake> snakes;

    private static Map map = new Map("map.txt");

    public List<Coordinate> getObstacles() {
        return obstacles;
    }

    public List<Snake> getSnakes() {
        return snakes;
    }


    private Map(String fileName) {
        try {
            BufferedReader mapReader = new BufferedReader(new FileReader(fileName));

            obstacles = new ArrayList<>();

            String line = mapReader.readLine();
            String[] parsed = line.split(" ");
            mapSize = parsed.length;

            for (int yIterator = 0; yIterator < mapSize; yIterator++) {
                if (yIterator != 0) {
                    line = mapReader.readLine();
                    parsed = line.split(" ");
                }
                for (int xIterator = 0; xIterator < mapSize; xIterator++) {
                    if (parsed[xIterator].equals("1")) {
                        obstacles.add(new Coordinate(xIterator, yIterator));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Map getInstance() {
        return map;
    }

    public int getMapSize() {
        return mapSize;
    }

    public JSONObject getJSONObjectFormat() {
        JSONObject jsonMap = new JSONObject();

        JSONArray obstaclesArray = new JSONArray();
        for (Coordinate coordinate: obstacles)
            obstaclesArray.put(coordinate.getJSONArrayFormat());

        jsonMap.put("obstacles", obstaclesArray);
        jsonMap.put("size", mapSize);

        return jsonMap;
    }

    public JSONObject get90DegreeJSONObjectFormat() {
        JSONObject jsonMap = new JSONObject();

        JSONArray obstaclesArray = new JSONArray();
        for (Coordinate coordinate: obstacles)
            obstaclesArray.put(Map.rotate90Degree(coordinate).getJSONArrayFormat());

        jsonMap.put("obstacles", obstaclesArray);
        jsonMap.put("size", mapSize);

        return jsonMap;
    }

    public JSONObject get180DegreeJSONObjectFormat() {
        JSONObject jsonMap = new JSONObject();

        JSONArray obstaclesArray = new JSONArray();
        for (Coordinate coordinate: obstacles)
            obstaclesArray.put(Map.rotate180Degree(coordinate).getJSONArrayFormat());

        jsonMap.put("obstacles", obstaclesArray);
        jsonMap.put("size", mapSize);

        return jsonMap;
    }

    public JSONObject get270DegreeJSONObjectFormat() {
        JSONObject jsonMap = new JSONObject();

        JSONArray obstaclesArray = new JSONArray();
        for (Coordinate coordinate: obstacles)
            obstaclesArray.put(Map.rotate270Degree(coordinate).getJSONArrayFormat());

        jsonMap.put("obstacles", obstaclesArray);
        jsonMap.put("size", mapSize);

        return jsonMap;
    }

    public boolean checkForObstacle(Coordinate coordinate) {
        for (Coordinate obstacle: obstacles)
            if (obstacle.equals(coordinate))
                return false;
        return true;
    }

    public boolean checkForBound(Coordinate coordinate) {
        boolean xOutOfBoundCondition = coordinate.getX() < 0 || coordinate.getX() > mapSize - 1;
        boolean yOutOfBoundCondition = coordinate.getY() < 0 || coordinate.getY() > mapSize - 1;
        return !(xOutOfBoundCondition || yOutOfBoundCondition);
    }

    public static Coordinate rotate90Degree(Coordinate coordinate) {
        int tmpX = coordinate.getX();
        int tmpY = coordinate.getY();
        return new Coordinate(map.getMapSize()-1-tmpX, tmpY);
//        return new Coordinate(map.getMapSize()-1-tmpY, tmpX);
    }

    public static List<Coordinate> rotate90Degree(List<Coordinate> coordinates) {
        List<Coordinate> rotatedCoordinates = new LinkedList<>();
        for (Coordinate coordinate: coordinates)
            rotatedCoordinates.add(rotate90Degree(coordinate));
        return rotatedCoordinates;
    }

    public static Coordinate rotate180Degree(Coordinate coordinate) {
        return rotate90Degree(rotate90Degree(coordinate));
    }

    public static List<Coordinate> rotate180Degree(List<Coordinate> coordinates) {
        List<Coordinate> rotatedCoordinates = new LinkedList<>();
        for (Coordinate coordinate: coordinates)
            rotatedCoordinates.add(rotate180Degree(coordinate));
        return rotatedCoordinates;
    }

    public static Coordinate rotate270Degree(Coordinate coordinate) {
        return rotate180Degree(rotate90Degree(coordinate));
    }

    public static List<Coordinate> rotate270Degree(List<Coordinate> coordinates) {
        List<Coordinate> rotatedCoordinates = new LinkedList<>();
        for (Coordinate coordinate: coordinates)
            rotatedCoordinates.add(rotate270Degree(coordinate));
        return rotatedCoordinates;
    }

    public void addSnake(Snake s){
        snakes.add(s);
    }
    @Override
    public String toString() {
        return getJSONObjectFormat().toString();
    }
}
