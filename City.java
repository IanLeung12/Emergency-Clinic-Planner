import java.util.ArrayList;
import java.util.Arrays;

public class City {
    private ArrayList<Community> communities;

    public City(ArrayList<Community> communities) {
        this.communities = communities;
        System.out.println(communities);
        this.addClinics();
        System.out.println(communities);
    }

    public void addClinics() {
        for (int i = 0; i < communities.size(); i ++) {
            Community community = communities.get(i);
            if (community.getConnections().length == 1) {
                makeClinic(communities.get(community.getConnections()[0]));
            } else if (community.getConnections().length ==0) {
                makeClinic(community);
            }
        }
    }

    public void makeClinic(Community community) {
        community.setClinic(true);
        int[] connections = community.getConnections();

        for (int i = 0; i < connections.length; i ++) {
            communities.get(connections[i]).setSafe(true);
        }
    }

    public ArrayList<Community> getCommunities() {
        return communities;
    }
}

