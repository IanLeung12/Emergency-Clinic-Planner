public class Community {
    private int id;
    private int[] connections;
    private boolean isSafe;

    Community(int id, int[] connections, boolean isSafe) {
        this.id = id;
        this.connections = connections;
        this.isSafe = isSafe;

    }
}
