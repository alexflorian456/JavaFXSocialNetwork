package Repository.InDataBase;

import Domain.Message;
import Domain.User;
import Exceptions.RepoException;
import Exceptions.ValidationException;
import Repository.Repository;
import Validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDBRepository implements Repository<Message> {

    private String url;
    private String username;
    private String password;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    @Override
    public Message findID(int id) {
        return null;
    }

    @Override
    public Message findID(int id1, int id2) {
        return null;
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Messages");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                int msg_id = resultSet.getInt("msg_id");
                int sender_id = resultSet.getInt("sender_id");
                int receiver_id = resultSet.getInt("receiver_id");
                String content = resultSet.getString("content");

                Message message = new Message(msg_id,sender_id,receiver_id,content);
                messages.add(message);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    public List<Message> getMessagesOf(int id1,int id2){
        List<Message> messages = new ArrayList<>();

        String sql = "select * from Messages where (sender_id=? and receiver_id=?) or (sender_id=? and receiver_id=?)";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1,id1);
            ps.setInt(2,id2);
            ps.setInt(3,id2);
            ps.setInt(4,id1);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                int msg_id = resultSet.getInt("msg_id");
                int sender_id = resultSet.getInt("sender_id");
                int receiver_id = resultSet.getInt("receiver_id");
                String content = resultSet.getString("content");

                Message message = new Message(msg_id,sender_id,receiver_id,content);
                messages.add(message);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public Message add(Message message) throws RepoException, ValidationException {
        String sql = "insert into Messages (sender_id,receiver_id,content) values (?,?,?)";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1,message.getSenderId());
            ps.setInt(2,message.getReceiverId());
            ps.setString(3,message.getContent());

            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message remove(Message message) throws RepoException, ValidationException {
        return null;
    }

    @Override
    public Message update(Message message) {
        return null;
    }
}
