import java.util.ArrayList;
import java.util.PriorityQueue;

public class City {
    private ArrayList<Community> communities;

    public City(ArrayList<Community> communities) {
        this.communities = communities;

        // Finds if some communities are safe
        for (int i = 0; i < communities.size(); i ++) {
            if (communities.get(i).isClinic()) {
                for (int c = 0; c < communities.get(i).getConnections().size(); c ++) {
                    communities.get(communities.get(i).getConnections().get(c)).setSafe(true);
                }
            }
        }
    }

    /**
     * locateClinics
     * runs greedy algorithm to find the optimal amount of clinics to place.
     * The algorithm uses a priority queue to find the unsafe community that is connected to the least unsafe neighbors
     * and sets its neighbor with the most unsafe neighbors to a clinic.
     */
    public void locateClinics() {
        PriorityQueue<Community> unsafeQueue= new PriorityQueue<>();

        for (int i = 0; i < communities.size(); i ++) {

            // Tallies each communities' number of unsafe neighbors
            int unsafeNum = 0;
            ArrayList<Integer> connections = communities.get(i).getConnections();
            for (int c = 0; c < connections.size(); c ++) {
                if (!communities.get(connections.get(c)).isSafe()) {
                    unsafeNum = unsafeNum + 1;
                }
            }
            communities.get(i).setUnsafeConnections(unsafeNum);

            // Adds unsafe communities to queue
            if (!communities.get(i).isSafe()) {
                unsafeQueue.offer(communities.get(i));
            }
        }

        Community connectedCom = null;

        // Continues until all communities are safe
        while (unsafeQueue.size() > 0) {

            //Retrieves the community with the least unsafe neighbors
            Community leastConnected = unsafeQueue.poll();
            Community mostConnected = leastConnected;

            // Finds the neighbor with the most unsafe neighbors
            for (int i = 0; i < leastConnected.getConnections().size(); i ++) {
                connectedCom = communities.get(leastConnected.getConnections().get(i));

                if ((leastConnected.getUnsafeConnections() == 0 || !connectedCom.isSafe())
                        && connectedCom.compareTo(mostConnected) > 0) {
                    mostConnected = connectedCom;
                }
            }

            // Sets clinic
            mostConnected.setClinic(true);
            mostConnected.setSafe(true);
            unsafeQueue.remove(mostConnected);

            // Recalibrates neighbors
            for (int i = 0; i < mostConnected.getConnections().size(); i ++) {
                connectedCom = communities.get(mostConnected.getConnections().get(i));
                connectedCom.setUnsafeConnections(connectedCom.getUnsafeConnections() - 1);

                // Recalibrates neighbors of neighbors
                if (!connectedCom.isSafe()) {
                    for (int c = 0; c < connectedCom.getConnections().size(); c ++) {
                        Community secondCommunity = communities.get(connectedCom.getConnections().get(c));

                        if (!secondCommunity.isSafe()) { // Element needs to be replaced in the queue to be sorted correctly
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

    /**
     * addClinic
     * turns a community into a clinic
     * @param community the community to turn into a clinic
     */
    public void addClinic(Community community) {
        community.setClinic(true);
        community.setSafe(true);

        // Sets neighbors to safe
        ArrayList<Integer> connections = community.getConnections();
        for (int c = 0; c < connections.size(); c ++) {
            communities.get(connections.get(c)).setSafe(true);
        }
    }

    /**
     * removeClinic
     * turns a clinic into a regular community
     * @param community the clinic to remove
     */
    public void removeClinic(Community community) {
        community.setClinic(false);

        // Rechecks safety of all affected communities
        checkSafety(community);
        ArrayList<Integer> connections = community.getConnections();
        for (int i = 0; i < connections.size(); i ++) {
            checkSafety(communities.get(connections.get(i)));
        }
    }

    /**
     * checkSafety
     * checks and sets a community to if they are safe or not
     * @param community the community to check
     */
    public void checkSafety(Community community) {
        ArrayList<Integer> connections = community.getConnections();
        community.setSafe(false);

        for (int i = 0; i < connections.size(); i ++) {
            if (communities.get(connections.get(i)).isClinic()) {
                community.setSafe(true);
            }
        }
    }

    /**
     * removeCommunity
     * removes a community based on its index
      * @param index the index of its community
     */
    public void removeCommunity(int index) {
        if ((index >=0) && (index < communities.size())) {
            Community removeCom = communities.get(index);
            removeCom.setClinic(false);

            // All communities must decrement their connection numbers as the removal causes larger numbers to decrease by one
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

