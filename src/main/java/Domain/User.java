package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity {
    private final int ID;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private final List<User> friends;

    public User(int ID, String username, String firstname, String lastname, String password) {
        this.ID = ID;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.friends = new ArrayList<>();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, username, friends);
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public String getPassword(){
        return password;
    }

    public List<User> getFriends() {
        return friends;
    }

    /**
     * adds friend to user's friend list
     *
     * @param friend user to add to this user's friend list
     */
    public void addFriend(User friend) {
        friends.add(friend);
    }

    /**
     * removes friend from user's friend list
     *
     * @param friend user to remove from this user's friend list
     * @return true if friend existed in this user's friend list, false otherwise
     */
    public boolean removeFriend(User friend) {
        boolean ok = false;
        for (User user :
                friends) {
            if (user.ID == friend.ID) {
                friends.remove(user);
                ok = true;
                break;
            }
        }
        return ok;
    }

    @Override
    public String toString() {
        return ID + "," + username;
    }
}
