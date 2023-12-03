import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("What is the name of the text file?");

        Scanner input = new Scanner(System.in);
        //City city = new City(readFile(input.nextLine()));
        City city = new City(readFile("map1.txt"));
        Visualizer visualizer = new Visualizer(city.getCommunities());
        input.close();
        


    }


    public static ArrayList<Community> readFile(String fileName) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(fileName));
        ArrayList<Community> communities = new ArrayList<>();

        String maptext = "";
        while (reader.hasNext()) {
            String[] strConnections = reader.nextLine().split(" ", -1);
            System.out.println(Arrays.toString(strConnections));
            int[] connections = new int[strConnections.length - 1];

            for (int j = 0; j < connections.length; j ++) {
                connections[j] = Integer.parseInt(strConnections[j]);
            }

            communities.add(new Community(connections, strConnections[strConnections.length - 1].equals("true"),
                    (int) (Math.random() * 1200), (int) (Math.random() *  600)));
        }

        reader.close();

        return communities;
    }

}
