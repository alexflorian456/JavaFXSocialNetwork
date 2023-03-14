package Repository.InDataBase;

import Domain.Friendship;
import Domain.FriendshipStatus;
import Domain.User;
import Exceptions.RepoException;
import Exceptions.ValidationException;
import Repository.Repository;
import Validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository implements Repository<User> {

    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findID(int id) {
        String sql = "select * from Users where u_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int id2 = resultSet.getInt("u_id");
                String username2 = resultSet.getString("username");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String password = resultSet.getString("password");
                User user = new User(id2, username2, firstname, lastname, password);
                return buildFriendships(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUsername(String username1) {
        String sql = "select * from Users where username = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username1);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int id2 = resultSet.getInt("u_id");
                String username2 = resultSet.getString("username");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String password = resultSet.getString("password");
                User user = new User(id2, username2, firstname, lastname, password);
                return buildFriendships(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User buildFriendships(User user) {
        List<User> users = getAll();
        for (User user2 :
                users) {
            if (user2.getID() == user.getID()) {
                return user2;
            }
        }
        return null;
    }

    @Override
    public User findID(int id1, int id2) {
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("u_id");
                String username2 = resultSet.getString("username");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String password = resultSet.getString("password");

                User user = new User(id, username2, firstname, lastname, password);
                users.add(user);
            }

            buildFriendships(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Iterable<Friendship> getAllFriendships() {
        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id1 = resultSet.getInt("u_id1");
                int id2 = resultSet.getInt("u_id2");
                LocalDateTime friendsfrom = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                String stringStatus = resultSet.getString("status");
                FriendshipStatus status;
                if (stringStatus.equals("PENDING")) {
                    status = FriendshipStatus.PENDING;
                } else {
                    status = FriendshipStatus.ACCEPTED;
                }

                Friendship friendship = new Friendship(id1, id2, friendsfrom, status);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    private void buildFriendships(List<User> users) {
        List<Friendship> friendships = (List<Friendship>) getAllFriendships();
        for (Friendship friendship :
                friendships) {
            User user1 = null;
            User user2 = null;
            int id1 = friendship.getUser1();
            int id2 = friendship.getUser2();
            for (User user :
                    users) {
                if (user.getID() == id1) {
                    user1 = user;
                }
                if (user.getID() == id2) {
                    user2 = user;
                }
            }
            if (user1 != null && user2 != null) {
                user1.addFriend(user2);
                user2.addFriend(user1);
            } else {
                System.out.println("Users not found when building friendships");
                return;
            }
        }
    }

    @Override
    public User add(User user) throws RepoException, ValidationException {
        validator.validate(user);

        Iterable<User> all = getAll();

        for (User user2 :
                all) {
            if (user2.getUsername().equals(user.getUsername()))
                throw new RepoException("Username already exists!");
        }

        String sql = "insert into Users (username, firstname, lastname, password) values (?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getLastname());
            ps.setString(4, user.getPassword());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public User remove(User user) throws RepoException, ValidationException {
        validator.validate(user);

        String sql = "delete from Users where u_id = ?";

        int length0 = ((ArrayList<User>) getAll()).size();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user.getID());

            ps.executeUpdate();

            int length1 = ((ArrayList<User>) getAll()).size();

            if (length1 == length0) {
                throw new RepoException("Non existent user!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    public User findUserPass(String user, String pass) {
        String sql = "SELECT * FROM Users WHERE username=? and password=?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,user);
            ps.setString(2,pass);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("u_id");
                String username2 = resultSet.getString("username");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String password = resultSet.getString("password");
                User user1 = new User(id, username2, firstname, lastname, password);
                return buildFriendships(user1);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }


}
