//Angkan Baidya
//112309655
//R01
import java.util.Comparator;

/**
 * compares the countFollower of two users
 */
public class FollowersComparator implements Comparator< User >{

    @Override
    public int compare(User o1, User o2) {
        User user1 = (User) o1;
        User user2 = (User) o2;
        return (user1.getCountFollower()-user2.getCountFollower());
    }

}
