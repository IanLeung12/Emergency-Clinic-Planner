import java.util.ArrayList;
import java.util.Arrays;

public class Community {
    private int[] connections;
    private boolean isClinic;
    private boolean isSafe;
    private int x, y;

    Community(int[] connections, boolean isClinic, int x, int y) {
        this.connections = connections;
        this.isClinic = isClinic;
        this.isSafe = isClinic;
        this.x = x;
        this.y = y;

    }

    public int[] getConnections() {
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
        return "\nConnected to: " + Arrays.toString(connections) + (isSafe ? " and is safe" : " and is not safe");
    }
}
