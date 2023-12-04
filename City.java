import java.util.ArrayList;
import java.util.Arrays;

public class City {
    private ArrayList<Community> communities;

    public City(ArrayList<Community> communities) {
        this.communities = communities;
        System.out.println(communities);
    }

    public void addClinics() {
        for (int i = 0; i < communities.size(); i ++) {
            Community community = communities.get(i);
            if (community.getConnections().size() == 1) {
                makeClinic(communities.get(community.getConnections().get(0)));
            } else if (community.getConnections().size() ==0) {
                makeClinic(community);
            }
        }
    }

    public void makeClinic(Community community) {
        community.setClinic(true);
        ArrayList<Integer> connections = community.getConnections();

        for (int i = 0; i < connections.size(); i ++) {
            communities.get(connections.get(i)).setSafe(true);
        }
    }

    public ArrayList<Community> getCommunities() {
        return communities;
    }
}

