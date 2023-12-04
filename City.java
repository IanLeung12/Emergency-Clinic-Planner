import java.util.ArrayList;
import java.util.Arrays;

public class City {
    private ArrayList<Community> communities;

    public City(ArrayList<Community> communities) {
        this.communities = communities;
        checkAll();
    }

    public void addClinics() {
        for (int i = 0; i < communities.size(); i ++) {
            Community community = communities.get(i);
            if (community.getConnections().size() == 1) {
                addClinic(communities.get(community.getConnections().get(0)));
            } else if (community.getConnections().size() ==0) {
                addClinic(community);
            }
        }
    }

    public void addClinic(Community community) {
        community.setClinic(true);
        ArrayList<Integer> connections = community.getConnections();

        for (int i = 0; i < connections.size(); i ++) {
            communities.get(connections.get(i)).setSafe(true);
        }
    }

    public void removeClinic(Community community) {
        community.setClinic(false);
        checkSafety(community);

        ArrayList<Integer> connections = community.getConnections();
        for (int i = 0; i < connections.size(); i ++) {
            checkSafety(communities.get(Integer.valueOf(connections.get(i))));
        }
    }

    public void checkSafety(Community community) {
        ArrayList<Integer> connections = community.getConnections();
        community.setSafe(false);
        if (community.isClinic()) {
            community.setSafe(true);
        } else {
            for (int i = 0; i < connections.size(); i ++) {
                if (communities.get(connections.get(i)).isClinic()) {
                    community.setSafe(true);
                }
            }
        }
    }

    public void checkAll() {
        for (int i = 0; i < communities.size(); i ++) {
            checkSafety(communities.get(i));
        }
    }


    public void removeCommunity(int index) {
        if ((index >=0) && (index < communities.size())) {
            Community removeCom = communities.get(index);
            for (int i = 0; i < communities.size(); i ++) {
                communities.get(i).decrementConnections(index);
            }
            communities.remove(index);
            checkAll();
        }
    }

    public ArrayList<Community> getCommunities() {
        return communities;
    }
}

