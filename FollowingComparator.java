//Angkan Baidya
//112309655
//R01

import java.util.Comparator;

/**
 * compares the countFollowing of two users
 */
public class FollowingComparator implements Comparator< User >{

    @Override
    public int compare(User o1, User o2) {
        User user1 = (User) o1;
        User user2 = (User) o2;
        return (user1.getCountFollowing() - user2.getCountFollowing());
    }


}
