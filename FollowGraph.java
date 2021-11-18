
//Angkan Baidya
//112309655
//R01


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Contains an adjacency matrix for the users.
 */
public class FollowGraph implements Serializable {


    private static final long serialVersionUID = 1L;
    // class FollowGraph attributes

    private ArrayList<User> users = new ArrayList<User>();
    public static final int MAX_USERS = 100;
    private boolean[][] connections = new boolean[MAX_USERS][MAX_USERS];;

    /**
     *  class FollowGraph constructor
     */
    public FollowGraph() {

    }

    /**
     * gets the connections
     * @param users
     * @param connections
     */
    public FollowGraph(ArrayList<User> users, boolean[][] connections) {
        this.users = users;
        this.connections = connections;
    }


    /**
     * Gets the users
     * @return
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * sets the users
     * @param users
     */

     public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    /**
     * gets the connections
     * @return
     */

     public boolean[][] getConnections() {
        return connections;
    }

    /**
     * sets the connections
     * @param connections
     */

     public void setConnections(boolean[][] connections) {
        this.connections = connections;
    }

    /**
     * gets max users
     * @return
     */


    public static int getMaxUsers() {
        return MAX_USERS;
    }


    /**
     * adds a new user if doesnt already exist
     */

    public void addUser(String userName) {
        User user = new User(userName);
        users.add(user);
        // save the followGraph after the adding the new user follow_graph.obj
        saveFollowGraph();
    }

    /**
     * adds a connection if doesnt exist
     * @param userFrom
     * @param userTo
     */
    public void addConnection(String userFrom, String userTo) {
        User user1 = userExists(userFrom);
        User user2 = userExists(userTo);
        // add the new connection
        this.connections[user1.getIndexPos()][user2.getIndexPos()] = true;
        user1.addFollowing();
        user2.addFollower();
        // save the followGraph after the adding the new connection
        saveFollowGraph();
    }

    /**
     *  removes a user if appropriate users are given (set value to false)
     * @param userName
     */
    public void removeUser(String userName) {
        // check if the user exists
        boolean remove = false;
        if (users != null) {
            for (User user : users) {
                if ((user.getUserName()).equalsIgnoreCase(userName)) {
                    // delete any connection this user has
                    for (User us : users) {
                        removeConnection(userName, us.getUserName());
                        removeConnection(us.getUserName(), userName);
                    }
                    // delete the user
                    users.remove(user);
                    // save the followGraph after removing the user
                    saveFollowGraph();
                    remove = true;
                    break;
                }
            }
        }
        if (!remove) {
            System.out.println("There is no user with this name, Please choose a valid user!");
        }

    }

    /**
     *   removes a connection if appropriate users are given (set value to false)
     * @param userFrom
     * @param userTo
     */
    public void removeConnection(String userFrom, String userTo) {

        boolean[][] connectionList = this.getConnections();
        User user1 = userExists(userFrom);
        User user2 = userExists(userTo);
        int pos1 = user1.getIndexPos();
        int pos2 = user2.getIndexPos();
        if (connectionList[pos1][pos2]) {
            connectionList[pos1][pos2] = false;
            // update the countFollowing and countFollowers for the 2 users
            user1.removeFollowing();
            user2.removeFollower();
        }
        // save the followGraph after removing the connection
        saveFollowGraph();

    }

    /**
     * returns a String representation of the path of the shortest path between the users
     * @param userFrom
     * @param userTo
     * @return
     */
    public String shortestPath(String userFrom, String userTo) {
        String shortestPath = "";
        int numberOfUsers = Integer.MAX_VALUE;
        List<String> allPaths = allPaths(userFrom, userTo);
        for (String path : allPaths) {
            String[] users = path.split(" -> ");
            if (numberOfUsers > users.length) {
                numberOfUsers = users.length;
                shortestPath = path;
            }
        }
        return shortestPath + "###" + numberOfUsers;
    }

    /**
     * returns a List Strings representing one of the paths between the users
     * @param userFrom
     * @param userTo
     * @return
     */
    public List<String> allPaths(String userFrom, String userTo) {
        User userSrc = userExists(userFrom);
        User userTarget = userExists(userTo);
        List<String> pathsList = new ArrayList<String>();
        if (userSrc != null && userTarget != null) {
            String path = userSrc.getUserName();
            findPath(userSrc, userTarget, path, pathsList);

        }
        return pathsList;
    }

    /**
     * finds the path finds the path between the users
     * @param userFrom
     * @param userTo
     * @param path
     * @param pathsList
     */
    private void findPath(User userFrom, User userTo, String path, List<String> pathsList) {
        List<User> followedUsers = getFollowedByUser(userFrom);

        for (User user : followedUsers) {
            if (!path.contains(user.getUserName())) {
                String newPath = path + " -> " + user.getUserName();
                if (user.getIndexPos() == userTo.getIndexPos()) {
                    pathsList.add(newPath);
                } else {
                    findPath(user, userTo, newPath, pathsList);
                }
            }
        }
    }

    /**
     * prints all users in the order based on the given Comparator
     * @param comp
     */
    public void printAllUsers(Comparator comp) {
        // sort the list of users
        Collections.sort(users, comp);
        // print the list of users
        System.out.println("Users:");
        System.out
                .println(String.format("%-30s %-20s %-20s", "User Name", "Number of Followers", "Number of Following"));
        // System.out.println("User Name Number of Followers Number Following");
        users.forEach(user -> System.out.println(String.format("%-40s %-20s %-20s", user.getUserName(),
                user.getCountFollower(), user.getCountFollowing())));
    }

    /**
     * prints all the followers of the given user
     * @param userName
     */
    public void printAllFollowers(String userName) {
        // controls on the accuracy of the userName should be done before calling this
        // method (in the FollowGraphDriver)
        User user1 = userExists(userName);
        User user2 = null;
        boolean[][] connectionList = this.getConnections();
        // check that the user exists
        if (user1 != null) {
            int i = user1.getIndexPos();
            System.out.println("List of the followers");
            for (int j = 0; j < connectionList[i].length; j++) {
                if (connectionList[j][i]) {
                    user2 = getUserByPosition(j);
                    System.out.println(user2.getUserName());
                }
            }
        } else {
            System.out.println("There is no user with this name, Please choose a valid user!");
        }
    }

    /**
     * prints all the users that the given user is following
     * @param userName
     */
    public void printAllFollowing(String userName) {
        // controls on the accuracy of the userName should be done before calling this
        // method (in the FollowGraphDriver)
        User user1 = userExists(userName);
        User user2 = null;
        boolean[][] connectionList = this.getConnections();
        // check that the user exists
        if (user1 != null) {
            int i = user1.getIndexPos();
            System.out.println("List of the followings");
            for (int j = 0; j < connectionList[i].length; j++) {
                if (connectionList[i][j]) {
                    user2 = getUserByPosition(j);
                    System.out.println(user2.getUserName());
                }
            }
        } else {
            System.out.println("There is no user with this name, Please choose a valid user!");
        }
    }

    /**
     *finds and returns all the loops in the graph
     * @return
     */
    public List<String> findAllLoops() {
        List<String> loopsList = new ArrayList<String>();

        for (User user : getUsers()) {
            List<Integer> visited = new ArrayList<Integer>();
            String loop = user.getUserName();
            // visited.add(user.getIndexPos());
            findLoop(user, user, loop, loopsList, visited);
        }
        return loopsList;
    }

    /**
     * Recursive method to fin a loop
     * @param userStart
     * @param userHead
     * @param loop
     * @param loopsList
     * @param visited
     */
    private void findLoop(User userStart, User userHead, String loop, List<String> loopsList, List<Integer> visited) {
        List<User> followedUsers = getFollowedByUser(userStart);

        for (User user : followedUsers) {
            if (!visited.contains(user.getIndexPos())) {

                if (user.getIndexPos() == userHead.getIndexPos()) {
                    String newPath = loop;
                    boolean found = checkIfPresent(loopsList, newPath);
                    if (!found) {
                        loopsList.add(newPath);
                    }
                } else {
                    visited.add(user.getIndexPos());
                    String newPath = loop + " -> " + user.getUserName();
                    findLoop(user, userHead, newPath, loopsList, visited);
                }
            }
        }

    }

    /**
     * Checks if present
     * @param loopsList
     * @param newPath
     * @return
     */
    private boolean checkIfPresent(List<String> loopsList, String newPath) {
        boolean check = false;
        for (String path : loopsList) {
            String[] paths = path.split(" -> ");
            String[] newPaths = newPath.split(" -> ");
            if (paths.length == newPaths.length) {
                List<String> pathsList = Arrays.asList(paths);
                List<String> newPathsList = Arrays.asList(newPaths);
                if (pathsList.containsAll(newPathsList)) {
                    return true;
                }
            }
        }
        return check;
    }

    /**
     * parses a file and add all users to the user list
     * @param filename
     */
    public void loadAllUsers(String filename) {
        try {
            // create a connection for reading the file users.txt
            BufferedReader userFile = new BufferedReader(new FileReader(filename));
            String nameUser = "";
            // Loop until you reach the end of the file
            while ((nameUser = userFile.readLine()) != null) {
                // Add user to list users : users
                User user = new User(nameUser);
                this.users.add(user);

            }
            // close the connection
            userFile.close();
            saveFollowGraph();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * parses a file and add all connections to the adjacency matrix
     * @param filename
     */
    public void loadAllConnections(String filename) {
        try {
            // create a connection for reading the file connections.txt
            BufferedReader connectionFile = new BufferedReader(new FileReader(filename));
            String connectionLine = "";
            // Loop until you reach the end of the file
            while ((connectionLine = connectionFile.readLine()) != null) {
                // get the name of the two users
                String name1 = "";
                String name2 = "";
                String[] tab = connectionLine.split(", ");
                // check if tab has 2 elements
                if (tab.length == 2) {
                    name1 = tab[0];
                    name2 = tab[1];
                    int position1 = -1;
                    int position2 = -1;
                    User user1 = null;
                    User user2 = null;
                    if (this.users != null) {
                        for (User user : users) {
                            if (user.getUserName().equalsIgnoreCase(name1)) {
                                user1 = user;
                                position1 = user.getIndexPos();
                            } else if (user.getUserName().equalsIgnoreCase(name2)) {
                                user2 = user;
                                position2 = user.getIndexPos();
                            }
                            if (position1 != -1 && position2 != -1) {
                                // only add the connection if both users exist else ignore the line
                                this.connections[position1][position2] = true;
                                System.out.println(connectionLine + " has been added");
                                // set the values of followers and following for each user
                                user1.addFollowing();
                                user2.addFollower();
                                saveFollowGraph();
                                break;
                            }

                        }
                    }
                }
            }
            // close the connection
            connectionFile.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * checks if user exists
     * @param userName
     * @return
     */
    public User userExists(String userName) {
        User user = null;
        if (this.users != null) {
            for (User us : users) {
                if ((us.getUserName()).equalsIgnoreCase(userName)) {
                    user = us;
                    break;
                }
            }
        }
        return user;
    }

    /**
     * gets the user by its position
     * @param position
     * @return
     */
    public User getUserByPosition(int position) {
        User user = null;
        if (this.users != null) {
            for (User us : users) {
                if (us.getIndexPos() == position) {
                    user = us;
                    break;
                }
            }
        }
        return user;
    }

    /**
     * gets connection by the users
     * @param userFrom
     * @param userTo
     * @return
     */
    public Boolean getConnectionByUsers(String userFrom, String userTo) {
        Boolean exist = false;

        boolean[][] connectionList = this.getConnections();
        User user1 = userExists(userFrom);
        User user2 = userExists(userTo);
        int pos1 = user1.getIndexPos();
        int pos2 = user2.getIndexPos();
        if (connectionList[pos1][pos2]) {
            exist = true;
        }
        return exist;
    }

    /**
     * gets the users followed by the user in input
     * @param user
     * @return
     */
    public List<User> getFollowedByUser(User user) {
        List<User> followingList = new ArrayList<User>();

        boolean[][] connectionList = this.getConnections();
        int pos1 = user.getIndexPos();
        for (int i = 0; i < this.getUsers().size(); i++) {
            if (connectionList[pos1][i])
                followingList.add(getUserByPosition(i));
        }
        return followingList;
    }

    /**
     *  saves the followGraph in the file follow_graph.obj
     */
    public void saveFollowGraph() {
        try {
            // create a connection for writing in the file follow_graph.obj
            ObjectOutputStream graphFile = new ObjectOutputStream(new FileOutputStream("follow_graph.obj"));
            // save the graph to the file
            graphFile.writeObject(this);
            graphFile.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * load the followGraph from the file follow_graph.obj
     * @return
     */public static FollowGraph loadFollowGraph() {
        FollowGraph followGraph = null;

        try {
            // create a connection for reading the file follow_graph.obj
            ObjectInputStream graphFile = new ObjectInputStream(new FileInputStream("follow_graph.obj"));
            followGraph = (FollowGraph) graphFile.readObject();
            User.setUserCount(followGraph.getUsers().size());
            graphFile.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return followGraph;
    }

    /**
     *prints the list of the connections in the followGraph
     */
    public void printConnections() {

        boolean[][] connectionList = this.getConnections();
        User user1 = null;
        User user2 = null;
        for (int i = 0; i < connectionList.length; i++) {
            for (int j = 0; j < connectionList[i].length; j++) {
                if (connectionList[i][j]) {
                    // retrieve the 2 users
                    user1 = getUserByPosition(i);
                    user2 = getUserByPosition(j);
                    // check if the 2 users exist
                    if (user1 != null && user2 != null) {
                        // display the connection on the console
                        System.out.println("connection : " + user1.getUserName() + " , " + user2.getUserName());
                    }
                }
            }
        }
    }

    /**
     * floyd Warshall method
     * @return
     */
    public int[][] floydWarshall() {

        double[][] dist = new double[getMaxUsers()][getMaxUsers()];
        for (double[] row : dist)
            Arrays.fill(row, Double.POSITIVE_INFINITY);

        int[][] weight = new int[connections.length][connections.length];
        for (int i = 0; i < connections.length; i++) {
            for (int j = 0; j < connections.length; j++) {
                weight[i][j] = (connections[i][j]) ? 1 : 0;
            }
        }

        int[][] next = new int[getMaxUsers()][getMaxUsers()];
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++)
                if (i != j)
                    next[i][j] = j + 1;
        }

        for (int k = 0; k < getMaxUsers(); k++)
            for (int i = 0; i < getMaxUsers(); i++)
                for (int j = 0; j < getMaxUsers(); j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
        return next;
    }


}
