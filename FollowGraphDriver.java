//Angkan Baidya
//112309655
//R01

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Sets all values for user input
 */
public class FollowGraphDriver {


    private static Scanner userInputScanner;

    private static final String CHOICE_USER = "U";

    private static final String CHOICE_CONNECTION = "C";
    private static final String CHOICE_LOAD_USERS = "AU";
    private static final String CHOICE_LOAD_CONNECTIONS = "AC";
    private static final String CHOICE_PRINT_USERS = "P";
    private static final String CHOICE_PRINT_LOOPS = "L";
    private static final String CHOICE_REMOVE_USER = "RU";
    private static final String CHOICE_REMOVE_CONNECTION = "RC";
    private static final String CHOICE_SHORTEST_PATH = "SP";
    private static final String CHOICE_ALL_PATHS = "AP";
    private static final String CHOICE_QUIT = "Q";
    private static final String CHOICE_USERS_NAME = "SA";
    private static final String CHOICE_USERS_FOLLOWER = "SB";
    private static final String CHOICE_USERS_FOLLOWING = "SC";

    private static final List<String> CHOICES_LIST_MENU = Arrays.asList(CHOICE_USER, CHOICE_CONNECTION,
            CHOICE_LOAD_USERS, CHOICE_LOAD_CONNECTIONS, CHOICE_PRINT_USERS, CHOICE_PRINT_LOOPS, CHOICE_REMOVE_USER,
            CHOICE_REMOVE_CONNECTION, CHOICE_SHORTEST_PATH, CHOICE_ALL_PATHS, CHOICE_QUIT);
    private static final List<String> CHOICES_PRINT_MENU = Arrays.asList(CHOICE_USERS_NAME, CHOICE_USERS_FOLLOWER,
            CHOICE_USERS_FOLLOWING, CHOICE_QUIT);

    public static void main(String[] args) {
        userInputScanner = new Scanner(System.in);
        FollowGraph followGraph = new FollowGraph();

        File graph = new File("follow_graph.obj");
        if (!graph.exists() || !graph.isFile()) {
            System.out.println("follow_graph.obj is not found. New FollowGraph object will be created.");
        } else {
            followGraph = FollowGraph.loadFollowGraph();
        }
        String choice = "";
        do {
            choice = menu();

            if (choice.equals(CHOICE_USER)) {
                addUser(followGraph);
            }
            else if (choice.equals(CHOICE_CONNECTION)) {
                addConnection(followGraph);
            }
            else if (choice.equals(CHOICE_LOAD_USERS)) {
                loadUsers(followGraph);
            }
            else if (choice.equals(CHOICE_LOAD_CONNECTIONS)) {
                loadConnections(followGraph);
            }
            else if (choice.equals(CHOICE_PRINT_USERS)) {
                printUsers(followGraph);
            }
            else if (choice.equals(CHOICE_PRINT_LOOPS)) {
                printLoops(followGraph);

            }
            else if (choice.equals(CHOICE_REMOVE_USER)) {
                removeUser(followGraph);
            }
            else if (choice.equals(CHOICE_REMOVE_CONNECTION)) {
                removeConnection(followGraph);
            }
            else if (choice.equals(CHOICE_SHORTEST_PATH)) {
                findShortestPath(followGraph);
            }
            else if (choice.equals(CHOICE_ALL_PATHS)) {
                findAllPaths(followGraph);
            }
        } while (!choice.equals(CHOICE_QUIT));

        System.out.println("FollowGraph object saved into file FollowGraph.obj.");
        followGraph.saveFollowGraph();
        System.out.println();
        System.out.println("Program terminating...");

    }

    /**
     * finds all paths
     * @param followGraph
     */
    private static void findAllPaths(FollowGraph followGraph) {
        String userSource = "";
        do {
            System.out.print("Please enter the name of the user source: ");
            userSource = validateString(userInputScanner.nextLine());
        } while (userSource.isEmpty());

        String userDistination = "";
        do {
            System.out.print("Please enter the name of the user distination: ");
            userDistination = validateString(userInputScanner.nextLine());
        } while (userDistination.isEmpty());
        List<String> paths = followGraph.allPaths(userSource, userDistination);
        if (paths.isEmpty()) {
            System.out.println("There are no loops.");
        } else {
            if (paths.size() == 1) {
                System.out.print("There is 1 path:");
            } else {
                System.out.println("There are a total of " + paths.size() + " paths:");
            }
            for (String path : paths) {
                System.out.println(path);
            }
        }
    }

    /**
     * finds the shortest path
     * @param followGraph
     */
    private static void findShortestPath(FollowGraph followGraph) {
        String userSource = "";
        do {
            System.out.print("Please enter the name of the user source: ");
            userSource = validateString(userInputScanner.nextLine());
        } while (userSource.isEmpty());

        String userDistination = "";
        do {
            System.out.print("Please enter the name of the user distination: ");
            userDistination = validateString(userInputScanner.nextLine());
        } while (userDistination.isEmpty());

        String shortestPath = followGraph.shortestPath(userSource, userDistination);
        String[] dataSplit = shortestPath.split("###");
        if (dataSplit.length == 2) {
            System.out.println("The shortest path is: " + dataSplit[0]);
            System.out.println("The number of users in this path is: " + dataSplit[1]);
        }

    }

    /**
     *  removes a user
     */
    private static void removeUser(FollowGraph followGraph) {

        String userName = "";
        do {
            System.out.print("Please enter the name of the user: ");
            userName = validateString(userInputScanner.nextLine());
            if (!userName.isEmpty()) {
                followGraph.removeUser(userName);
            }

        } while (userName.isEmpty());
    }

    /**
     * Prints the loops
     * @param followGraph
     */
    private static void printLoops(FollowGraph followGraph) {

        List<String> loops = followGraph.findAllLoops();
        if (loops.isEmpty()) {
            System.out.println("There are no loops.");
        } else {
            if (loops.size() == 1) {
                System.out.print("There is 1 loop:");
            } else {
                System.out.println("There are a total of " + loops.size() + " loops:");
            }
            for (String path : loops) {
                System.out.println(path);
            }
        }
    }

    /**
     * Prints all the users
     * @param followGraph
     */
    private static void printUsers(FollowGraph followGraph) {


        String choiceSort = "";
        do {
            choiceSort = menuSort();

            if (choiceSort.equals(CHOICE_USERS_NAME)) {
                Comparator comparator = new NameComparator();
                followGraph.printAllUsers(comparator);
            }
            else if (choiceSort.equals(CHOICE_USERS_FOLLOWER)) {
                Comparator comparator = new FollowersComparator();
                followGraph.printAllUsers(comparator.reversed());
            }
            else if (choiceSort.equals(CHOICE_USERS_FOLLOWING)) {
                Comparator comparator = new FollowingComparator();
                followGraph.printAllUsers(comparator.reversed());
            }
        } while (!choiceSort.equals(CHOICE_QUIT));

    }

    /**
     * Loads the connections
     * @param followGraph
     */
    private static void loadConnections(FollowGraph followGraph) {
        String fileName = "";
        if (followGraph.getUsers().isEmpty()) {
            System.out.println("Please load the users file first");
        } else {
            do {
                System.out.print("Enter the file name: ");
                fileName = validateString(userInputScanner.nextLine());
                File file = new File(fileName);
                if (!file.exists() || !file.isFile()) {
                    System.out.println("The name of the file does not exist.");
                } else {
                    if (followGraph.getUsers().isEmpty()) {
                        System.out.println("Please load the users file first");
                    }

                    followGraph.loadAllConnections(fileName);

                }
            } while (fileName.isEmpty());

        }
    }

    /**
     * Loads the users
     * @param followGraph
     */
    private static void loadUsers(FollowGraph followGraph) {

        String fileName = "";
        do {
            System.out.print("Enter the file name: ");
            fileName = validateString(userInputScanner.nextLine());
            File file = new File(fileName);
            if (!file.exists() || !file.isFile()) {
                System.out.println("The name of the file does not exist.");
            } else {
                followGraph.loadAllUsers(fileName);
                followGraph.getUsers().forEach(user -> System.out.println(user.getUserName() + " has been added"));

            }

        } while (fileName.isEmpty());

    }

    /**
     * Adds a connection
     * @param followGraph
     */
    private static void addConnection(FollowGraph followGraph) {
        String nameUsr1 = "";
        String nameUsr2 = "";

        do {
            System.out.print("Please enter the source of the connection to add: ");
            nameUsr1 = validateString(userInputScanner.nextLine());
            User user1 = followGraph.userExists(nameUsr1);
            if (user1 == null) {
                System.out.println("There is no user with this name, Please choose a valid user! ");
                nameUsr1 = "";
            }
        } while (nameUsr1.isEmpty());

        if (!nameUsr1.isEmpty()) {
            do {
                System.out.print("Please enter the dest of the connection to add: ");
                nameUsr2 = validateString(userInputScanner.nextLine());
                User user2 = followGraph.userExists(nameUsr2);
                if (user2 == null) {
                    System.out.println("There is no user with this name, Please choose a valid user! ");
                    nameUsr2 = "";
                } else {
                    followGraph.addConnection(nameUsr1, nameUsr2);
                }
            }
            while (nameUsr2.isEmpty());

        }
    }

    /**
     * removes a connection
     * @param followGraph
     */
    private static void removeConnection(FollowGraph followGraph) {
        String nameUsr1 = "";
        String nameUsr2 = "";

        do {
            System.out.print("Please enter the source of the connection to remove: ");
            nameUsr1 = validateString(userInputScanner.nextLine());
            User user1 = followGraph.userExists(nameUsr1);
            if (user1 == null) {
                System.out.println("There is no user with this name, Please choose a valid user! ");
                nameUsr1 = "";
            }
        } while (nameUsr1.isEmpty());

        if (!nameUsr1.isEmpty()) {
            do {
                System.out.print("Please enter the dest of the connection to remove: ");
                nameUsr2 = validateString(userInputScanner.nextLine());
                User user2 = followGraph.userExists(nameUsr2);
                if (user2 == null) {
                    System.out.println("There is no user with this name, Please choose a valid user! ");
                    nameUsr2 = "";
                } else {
                    if (!followGraph.getConnectionByUsers(nameUsr1, nameUsr2)) {
                        System.out.println("There is no connection between these two users! ");
                    } else {
                        followGraph.removeConnection(nameUsr1, nameUsr2);
                    }
                }
            }
            while (nameUsr2.isEmpty());

        }
    }

    /**
     * Adds a new user
     * @param followGraph
     */
    private static void addUser(FollowGraph followGraph) {
        if (followGraph.getUsers().size() >= followGraph.MAX_USERS) {
            System.out.println("You have reached the maximum number of users!");
        } else {
            String userName = "";
            do {
                System.out.print("Please enter the name of the user: ");
                userName = validateString(userInputScanner.nextLine());
                User user = followGraph.userExists(userName);
                if (user != null) {
                    System.out.println("There is an other user with the same name, Please choose an other one! ");
                    userName = "";
                } else {
                    followGraph.addUser(userName);
                }

            } while (userName.isEmpty());
        }
    }

    /**
     * validate the strings values entered by the user
     * @param value
     * @return
     */
    private static String validateString(String value) {
        String word = "";

        if (!value.isEmpty()) {
            word = value;
        } else
            System.out.println("You can not leave this field empty.");

        return word;
    }

    /**
     * Display menu and read user's choice
     * @return
     */
    private static String menu() {

        System.out.println();
        System.out.println("************ Menu ************");
        System.out.println("(U)  Add User");
        System.out.println("(C)  Add Connection");
        System.out.println("(AU) Load all Users");
        System.out.println("(AC) Load all Connections");
        System.out.println("(P)  Print all Users");
        System.out.println("(L)  Print all Loops");
        System.out.println("(RU) Remove User");
        System.out.println("(RC) Remove Connection");
        System.out.println("(SP) Find Shortest Path");
        System.out.println("(AP) Find All Paths");
        System.out.println("(Q)  Quit");
        System.out.println();
        String userChoice = "";
        while (true) {
            System.out.print("Enter a selection: ");

            userChoice = userInputScanner.nextLine().toUpperCase();
            if (CHOICES_LIST_MENU.contains(userChoice)) {
                return userChoice;
            } else {
                System.out.println("Your choice is incorrect");
            }

        }

    }

    /**
     *  Display the sort users menu and read the choice
     * @return
     */
    private static String menuSort() {


        System.out.println("(SA) Sort Users by Name");
        System.out.println("(SB) Sort Users by Number of Followers");
        System.out.println("(SC) Sort Users by Number of Following");
        System.out.println("(Q)  Quit");
        System.out.println();
        String userChoice = "";
        while (true) {
            System.out.print("Enter a selection: ");

            userChoice = userInputScanner.nextLine().toUpperCase();
            if (CHOICES_PRINT_MENU.contains(userChoice)) {
                return userChoice;
            } else {
                System.out.println("Your choice is incorrect");
            }

        }

    }

}
