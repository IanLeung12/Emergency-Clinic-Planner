import java.util.ArrayList;

public class Community implements Comparable<Community>{
    private ArrayList<Integer> connections;
    private boolean isClinic;
    private boolean safe;
    private int x, y;
    private int unsafeConnections;

    Community(int x, int y, boolean isClinic, ArrayList<Integer> connections) {
        this.connections = connections;
        this.isClinic = isClinic;
        this.safe = isClinic;
        this.x = x;
        this.y = y;
        this.unsafeConnections = -1;

    }

    public boolean collides(int x, int y) {
        return Math.sqrt(Math.pow((this.x + 38 - x), 2) + Math.pow((this.y + 96 - y), 2)) <= 32;
    }

    public void translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    public boolean removeConnection(int otherIndex) {
        return connections.remove(Integer.valueOf(otherIndex));
    }

    public void decrementConnections(int min) {
        for (int i = 0; i < connections.size(); i ++) {
            if (connections.get(i) > min) {
                connections.set(i, connections.get(i) - 1);
            } else if (connections.get(i) == min) {
                connections.remove(Integer.valueOf(min));
            }
        }
    }

    public int getUnsafeConnections() {
        return unsafeConnections;
    }

    public void setUnsafeConnections(int unsafeConnections) {
        this.unsafeConnections = unsafeConnections;
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
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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


    @Override
    public int compareTo(Community other) {
        return this.getUnsafeConnections() - other.unsafeConnections;
    }
}
