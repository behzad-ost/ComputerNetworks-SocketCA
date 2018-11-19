package server.movingObjects;

import common.Coordinate;

import org.json.JSONArray;
import server.Map;

import java.util.List;
import java.util.Random;


public class Food {
    private static Food food = new Food();
    private Food() {
        foodPosition = new Coordinate(7, 7);
    }
    public static Food getInstance() { return food; }

    private Coordinate foodPosition;
    public JSONArray getJSONArrayFormat() {
        return foodPosition.getJSONArrayFormat();
    }
    public Coordinate getFoodPosition() {
        return foodPosition;
    }

    public void generateFood() {
        List<Coordinate> obstacles = Map.getInstance().getObstacles();
        List<Snake> snakes = Map.getInstance().getSnakes();

        Coordinate newPosition = randomPoint(0,Map.getInstance().getMapSize());
        while (Contains(newPosition,obstacles,snakes)) {
            newPosition = randomPoint(0,9);
        }
        System.out.println(newPosition);

        foodPosition = newPosition;
    }

    private boolean Contains(Coordinate newPosition, List<Coordinate> obstacles, List<Snake> snakes) {
        for(int i = 0 ; i < obstacles.size() ; i++){
            if(obstacles.get(i).getX() == newPosition.getX() &&
               obstacles.get(i).getY() == newPosition.getY() ){
                System.out.println("O: "+ obstacles.get(i) + " P: "+ newPosition);
                return true;
            }
        }
//        for(int i = 0 ; i < snakes.size() ; i++){
//            if(snakes.get(i) == null)
//                continue;
//            for(int j = 0 ; j < snakes.get(i).getSnakeArray().size() ; j++){
//                if(snakes.get(i).getSnakeArray().get(j).getX() == newPosition.getX() &&
//                   snakes.get(i).getSnakeArray().get(j).getY() == newPosition.getY() ){
//                    return true;
//                }
//            }
//        }
        return false;
    }

    private static Coordinate randomPoint(int min, int max){
        Random random = new Random();
        int x = random.nextInt(max - min) + min;
        int y = random.nextInt(max - min) + min;
        Coordinate newCordinate = new Coordinate(x,y);
        return newCordinate;
    }

}