package common;

import org.json.JSONArray;

public class Coordinate {
    private int x, y;
    public Coordinate(int y, int x) {
        this.x = x;
        this.y = y;
    }
    public Coordinate(Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate))
            return false;

        Coordinate sec = (Coordinate)o;
        return this.x == sec.x && this.y == sec.y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
    public JSONArray getJSONArrayFormat() {
        JSONArray jsonCoordinate = new JSONArray();
        jsonCoordinate.put(x);
        jsonCoordinate.put(y);
        return jsonCoordinate;
    }
}