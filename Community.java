import java.util.ArrayList;
import java.util.Arrays;

public class Community {
    private ArrayList<Integer> connections;
    private boolean isClinic;
    private boolean isSafe;
    private int x, y;

    Community(int x, int y, boolean isClinic, ArrayList<Integer> connections) {
        this.connections = connections;
        this.isClinic = isClinic;
        this.isSafe = isClinic;
        this.x = x;
        this.y = y;

    }

    public boolean collides(int x, int y) {
        return Math.sqrt(Math.pow((this.x + 40 - x), 2) + Math.pow((this.y + 100 - y), 2)) <= 30;
    }


    public void translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    public ArrayList<Integer> getConnections() {
        return connections;
    }

    public boolean isClinic() {
        return isClinic;
    }

    public void setClinic(boolean clinic) {
        isClinic = clinic;
    }

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        String str = "";
        str = this.x + " " + this.y + " " + this.isClinic;
        for (int i =0; i < connections.size(); i ++) {
            str = str + " " + connections.get(i);
        }
        return str;
    }

}
