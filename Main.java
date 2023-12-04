import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        System.out.println("What is the name of the text file? (without .txt)");

        Scanner input = new Scanner(System.in);
        City city = new City(readFile(input.nextLine() + ".txt"));
        input.close();
        Visualizer visualizer = new Visualizer(city);

        while(true) {
            visualizer.refresh();
            Thread.sleep(10);
        }

        


    }


    public static ArrayList<Community> readFile(String fileName) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(fileName));
        ArrayList<Community> communities = new ArrayList<>();

        String maptext = "";
        while (reader.hasNext()) {
            String[] strConnections = reader.nextLine().split(" ", -1);
            System.out.println(Arrays.toString(strConnections));
            ArrayList<Integer> connections = new ArrayList<>(strConnections.length - 1);

            for (int j = 3; j < strConnections.length; j ++) {
                connections.add(Integer.parseInt(strConnections[j]));
            }

            communities.add(new Community(Integer.parseInt(strConnections[0]), Integer.parseInt(strConnections[1]),
                    strConnections[2].equals("true"), connections));
        }

        reader.close();

        return communities;
    }



}
