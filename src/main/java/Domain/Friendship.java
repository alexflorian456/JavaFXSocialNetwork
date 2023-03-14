package Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity {
    private int user1;
    private int user2;
    private LocalDateTime friendsFrom;

    private FriendshipStatus status;

    public Friendship(int user1, int user2, LocalDateTime friendsFrom,FriendshipStatus status) {
        this.user1 = user1;
        this.user2 = user2;
        this.friendsFrom = friendsFrom;
        this.status=status;
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2)) ||
                (Objects.equals(user1, that.user2) && Objects.equals(user2, that.user1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }

    @Override
    public String toString() {
        return user1 + "," + user2 + "," + friendsFrom.toString();
    }
}
