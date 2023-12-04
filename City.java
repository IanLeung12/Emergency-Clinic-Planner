import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class City {
    private ArrayList<Community> communities;

    public City(ArrayList<Community> communities) {
        this.communities = communities;

        for (int i = 0; i < communities.size(); i ++) {
            if (communities.get(i).isClinic()) {
                for (int c = 0; c < communities.get(i).getConnections().size(); c ++) {
                    communities.get(communities.get(i).getConnections().get(c)).setSafe(true);
                }
            }
        }
    }

    public void locateClinics() {
        PriorityQueue<Community> unsafeQueue= new PriorityQueue<>();
        for (int i = 0; i < communities.size(); i ++) {

            int unsafeNum = 0;
            ArrayList<Integer> connections = communities.get(i).getConnections();
            for (int c = 0; c < connections.size(); c ++) {
                if (!communities.get(connections.get(c)).isSafe()) {
                    unsafeNum = unsafeNum + 1;
                }
            }
            communities.get(i).setUnsafeConnections(unsafeNum);

            if (!communities.get(i).isSafe()) {
                unsafeQueue.offer(communities.get(i));
            }
        }

        Community connectedCom = null;
        while (unsafeQueue.size() > 0) {
            Community leastConnected = unsafeQueue.poll();
            Community mostConnected = leastConnected;
            if (leastConnected.getUnsafeConnections() == 0) {
                mostConnected = communities.get(leastConnected.getConnections().get(0));
            } else {
                for (int i = 0; i < leastConnected.getConnections().size(); i ++) {
                    connectedCom = communities.get(leastConnected.getConnections().get(i));
                    if ((!connectedCom.isSafe()) && (connectedCom.compareTo(mostConnected) > 0)) {
                        mostConnected = connectedCom;
                    }
                }
            }
            mostConnected.setSafe(true);
            mostConnected.setClinic(true);
            unsafeQueue.remove(mostConnected);

            for (int i = 0; i < mostConnected.getConnections().size(); i ++) {
                connectedCom = communities.get(mostConnected.getConnections().get(i));
                connectedCom.setUnsafeConnections(connectedCom.getUnsafeConnections() - 1);
                if (!connectedCom.isSafe()) {
                    for (int c = 0; c < connectedCom.getConnections().size(); c ++) {
                        Community secondCommunity = communities.get(connectedCom.getConnections().get(c));
                        if (!secondCommunity.isSafe()) {
                            unsafeQueue.remove(secondCommunity);
                            secondCommunity.setUnsafeConnections(secondCommunity.getUnsafeConnections() - 1);
                            unsafeQueue.offer(secondCommunity);
                        } else {
                            secondCommunity.setUnsafeConnections(secondCommunity.getUnsafeConnections() - 1);
                        }

                    }
                }
                connectedCom.setSafe(true);
                unsafeQueue.remove(connectedCom);
            }
        }
    }
    public void addClinic(Community community) {
        community.setClinic(true);
        community.setSafe(true);
        ArrayList<Integer> connections = community.getConnections();
        for (int c = 0; c < connections.size(); c ++) {
            communities.get(connections.get(c)).setSafe(true);
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

        for (int i = 0; i < connections.size(); i ++) {
            if (communities.get(connections.get(i)).isClinic()) {
                community.setSafe(true);
            }
        }
    }

    public void removeCommunity(int index) {
        if ((index >=0) && (index < communities.size())) {
            Community removeCom = communities.get(index);
            removeCom.setClinic(false);
            for (int i = 0; i < communities.size(); i ++) {
                communities.get(i).decrementConnections(index);
                checkSafety(communities.get(i));
            }
            communities.remove(index);
        }
    }

    public ArrayList<Community> getCommunities() {
        return communities;
    }
}

