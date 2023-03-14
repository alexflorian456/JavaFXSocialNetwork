package Repository.InDataBase;

import Domain.Friendship;
import Domain.FriendshipStatus;
import Exceptions.RepoException;
import Exceptions.ValidationException;
import Repository.Repository;
import Validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDBRepository implements Repository<Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship findID(int id) {
        return null;
    }

    @Override
    public Friendship findID(int id1, int id2) {
        String sql = "select * from Friendships where (u_id1 = ? and u_id2 = ?) or (u_id1 = ? and u_id2 = ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.setInt(3, id2);
            ps.setInt(4, id1);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                LocalDateTime friendsfrom = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                FriendshipStatus status;
                String stringStatus;
                stringStatus = resultSet.getString("status");
                if (stringStatus.equals("PENDING")) {
                    status = FriendshipStatus.PENDING;
                } else {
                    status = FriendshipStatus.ACCEPTED;
                }

                return new Friendship(id1, id2, friendsfrom, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Friendship> getAll() {
        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id1 = resultSet.getInt("u_id1");
                int id2 = resultSet.getInt("u_id2");
                LocalDateTime friendsfrom = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                FriendshipStatus status;
                String stringStatus;
                stringStatus = resultSet.getString("status");
                if (stringStatus.equals("PENDING")) {
                    status = FriendshipStatus.PENDING;
                } else {
                    status = FriendshipStatus.ACCEPTED;
                }

                Friendship friendship = new Friendship(id1, id2, friendsfrom,status);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship add(Friendship friendship) throws RepoException, ValidationException {
        validator.validate(friendship);

        Iterable<Friendship> all = getAll();

        for (Friendship friendship1 :
                all) {
            if (friendship.equals(friendship1)) {
                throw new RepoException("Friendship already exists!");
            }
        }

        String sql = "insert into Friendships (u_id1,u_id2,friendsfrom,status) values (?,?,current_timestamp,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, friendship.getUser1());
            ps.setInt(2, friendship.getUser2());

            String stringStatus;
            FriendshipStatus status = friendship.getStatus();
            if(status==FriendshipStatus.PENDING){
                stringStatus="PENDING";
            } else {
                stringStatus="ACCEPTED";
            }

            ps.setString(3,stringStatus);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Friendship remove(Friendship friendship) throws RepoException, ValidationException {
        validator.validate(friendship);
        int id1 = friendship.getUser1();
        int id2 = friendship.getUser2();

        String sql = "delete from Friendships where (u_id1 = ? and u_id2 = ?) or (u_id1 = ? and u_id2 = ?)";

        int length0 = ((ArrayList<Friendship>) getAll()).size();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.setInt(3, id2);
            ps.setInt(4, id1);

            ps.executeUpdate();

            int length1 = ((ArrayList<Friendship>) getAll()).size();

            if (length1 == length0) {
                throw new RepoException("Non existent friendship!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Friendship update(Friendship friendship) {
        return null;
    }
}
