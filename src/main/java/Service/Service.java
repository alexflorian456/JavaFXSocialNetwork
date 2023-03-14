package Service;

import DTO.MessageDTO;
import DTO.UserFriendshipDTO;
import Domain.Friendship;
import Domain.FriendshipStatus;
import Domain.Message;
import Domain.User;
import Exceptions.CorruptionException;
import Exceptions.RepoException;
import Exceptions.ServiceException;
import Exceptions.ValidationException;
import Repository.InDataBase.FriendshipDBRepository;
import Repository.InDataBase.MessageDBRepository;
import Repository.InDataBase.UserDBRepository;
import Utils.Observer.Observable;
import Utils.Observer.Observer;
import Validators.FriendshipValidator;
import Validators.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Service implements Observable {
    /*UserFileRepository userRepository;
    FriendshipFileRepository friendshipRepository;

    String userFile;
    String friendFile;*/

    UserDBRepository userRepository;
    FriendshipDBRepository friendshipRepository;
    MessageDBRepository messageDBRepository;

    private List<Observer> observers=new ArrayList<>();

    public Service(String url, String username, String password) throws CorruptionException {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        this.userRepository = new UserDBRepository(url,username,password,new UserValidator());
        this.friendshipRepository = new FriendshipDBRepository(url,username,password,new FriendshipValidator());
        this.messageDBRepository = new MessageDBRepository(url,username,password);
        buildFriendships();
    }

    private void buildFriendships() {
        for (Friendship friendship :
                friendshipRepository.getAll()) {
            try {
                addFriend(friendship.getUser1(),friendship.getUser2());
            } catch (ServiceException ignored) {

            }
        }
    }

    public void addMessage(int sender_id,int receiver_id, String content) throws ValidationException, RepoException {
        Message message = new Message(1,sender_id,receiver_id,content);
        messageDBRepository.add(message);
        notifyObservers();
    }

    public List<MessageDTO> getMessagesOf(int id1, int id2){
        List<Message> messages = messageDBRepository.getMessagesOf(id1,id2);
        String username1 = userRepository.findID(id1).getUsername();
        String username2 = userRepository.findID(id2).getUsername();
        List<MessageDTO> messageDTOS = new ArrayList<>();
        for (Message message :
                messages) {
            String username;
            String content = message.getContent();
            if(message.getSenderId()==id1){
                username=username1;
            }else{
                username=username2;
            }
            MessageDTO messageDTO = new MessageDTO(username,content);
            messageDTOS.add(messageDTO);
        }
        return messageDTOS;
    }

    public void addUser(String username,String firstname, String lastname, String password) throws ValidationException, RepoException {
        User user = new User(1,username,firstname,lastname,password);
        userRepository.add(user);
        notifyObservers();
    }

    /**
     * removes user from userRepository
     *
     * @param user user to remove from userRepository
     * @throws ValidationException if user is not valid (id less or equal to 0 or empty username)
     * @throws RepoException       if user doesn't exist in userRepository
     */
    public void removeUser(User user) throws ValidationException, RepoException {
        List<User> users = userRepository.getAll();
        for (User user2 :
                users) {
            List<User> friends = user2.getFriends();
            for (User user3 :
                    friends) {
                if (user3.equals(user)) {
                    user2.removeFriend(user);
                    friendshipRepository.remove(friendshipRepository.findID(user.getID(),user2.getID()));
                    break;
                }
            }
        }
        userRepository.remove(user);
        notifyObservers();
    }

    /**
     * adds user with id = ID1 to user with id = ID2's friend list and vice versa
     *
     * @param ID1 id of first user
     * @param ID2 if of second user
     * @throws ServiceException if either of the users do not exist in userRepository
     */
    public void addFriend(int ID1, int ID2) throws ServiceException {
        if (userRepository.findID(ID1) == null || userRepository.findID(ID2) == null) {
            throw new ServiceException("User doesn't exist!");
        }
        if (userRepository.findID(ID1).getFriends().contains(userRepository.findID(ID2)) ||
                userRepository.findID(ID2).getFriends().contains(userRepository.findID(ID1))) {
            throw new ServiceException("Users are already friends!");
        }
        if (ID1 != ID2) {
            User trueUser1 = userRepository.findID(ID1);
            User trueUser2 = userRepository.findID(ID2);
            List<User> users = userRepository.getAll();
            for (User user :
                    users) {
                if (user.getID() == ID1) {
                    user.addFriend(trueUser2);
                } else if (user.getID() == ID2) {
                    user.addFriend(trueUser1);
                    try {
                        if(friendshipRepository.findID(ID1,ID2)==null) {
                            friendshipRepository.add(new Friendship(ID1, ID2, LocalDateTime.now(), FriendshipStatus.PENDING));
                            notifyObservers();
                        }
                    } catch (RepoException e) {
                        e.print();
                    } catch (ValidationException e) {
                        e.print();
                    }
                }
            }
        } else {
            throw new ServiceException("User can't be his own friend!");
        }
    }

    /**
     * removes user with id = ID1 from user with id = ID2's friend list and vice versa
     *
     * @param ID1 id of the first user
     * @param ID2 id of the second user
     * @throws ServiceException if either of the users do not exist in userRepository
     */
    public void removeFriend(int ID1, int ID2) throws ServiceException {
        if (userRepository.findID(ID1) == null || userRepository.findID(ID2) == null) {
            throw new ServiceException("User doesn't exist!");
        }
        if (ID1 != ID2) {
            boolean ok = false;
            List<User> users = userRepository.getAll();
            for (User user :
                    users) {
                if (user.getID() == ID1) {
                    ok = user.removeFriend(userRepository.findID(ID2));
                    try {
                        friendshipRepository.remove(friendshipRepository.findID(ID1, ID2));
                        notifyObservers();
                    } catch (RepoException e) {
                        e.print();
                    } catch (ValidationException e) {
                        e.print();
                    }
                }
                if (user.getID() == ID2)
                    user.removeFriend(userRepository.findID(ID1));
            }
            if (!ok) {
                throw new ServiceException("Users aren't friends!");
            }
        } else {
            throw new ServiceException("User can't be his own friend!");
        }
    }

    /**
     * prints all users from userRepository
     */
    public void printUsers() {
        List<User> users = userRepository.getAll();
        for (User user :
                users) {
            System.out.println(user.getID() + " " + user.getUsername());
        }
    }

    /**
     * prints friends of user with id = id
     *
     * @param id id of user
     * @throws ServiceException if user doesnt exist in userRepository
     */
    public void printFriends(int id) throws ServiceException {
        if (userRepository.findID(id) == null) {
            throw new ServiceException("Non existent user!");
        }
        List<User> users = userRepository.getAll();
        for (User user2 :
                users) {
            if (user2.getID() == id) {
                List<User> friends = user2.getFriends();
                for (User friend :
                        friends) {
                    System.out.println(friend.getID() + " " + friend.getUsername());
                }
            }
        }
    }

    /**
     * prints the number of connected components in the social network
     */
    public void communities() {
        int nr = 0;
        List<User> copy = new ArrayList<>(userRepository.getAll());
        while (!copy.isEmpty()) {
            bfs(copy);
            nr++;
        }
        System.out.printf("There are %d communities\n", nr);
    }

    private List<User> bfsList(List<User> copy, User source) {
        Queue<User> queue = new LinkedList<>();
        List<User> visited = new ArrayList<>();
        queue.add(source);
        visited.add(source);
        while (!queue.isEmpty()) {
            User user = queue.remove();
            for (User friend :
                    user.getFriends()) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                }
                if (copy.contains(friend)) {
                    queue.add(friend);
                }
            }
            copy.remove(user);
        }
        return visited;
    }

    /**
     * bfs algorithm to find all the users in one connected component
     *
     * @param copy copy of userRepository, from which we remove the Users visited by bfs
     */
    private void bfs(List<User> copy) {
        Queue<User> queue = new LinkedList<>();
        queue.add(copy.remove(0));
        while (!queue.isEmpty()) {
            User user = queue.remove();
            for (User friend :
                    user.getFriends()) {
                if (copy.contains(friend)) {
                    queue.add(friend);
                }
            }
            copy.remove(user);
        }
    }

    public void biggestCommunity() {
        List<User> community = new ArrayList<>();
        for (User user :
                userRepository.getAll()) {
            if (longestRoad(userRepository.getAll(), user).size() > community.size()) {
                List<User> copy = new ArrayList<>(userRepository.getAll());
                community = bfsList(copy, user);
            }
        }
        System.out.println("The biggest community is:");
        for (User user :
                community) {
            System.out.println(user.getID() + " " + user.getUsername());
        }
    }

    private List<User> longestRoad(List<User> users, User source) {
        if (users.isEmpty()) {
            return null;
        }
        if (source.getFriends().isEmpty()) {
            List<User> road = new ArrayList<>();
            road.add(source);
            return road;
        }
        boolean ok = false;
        for (User friend :
                source.getFriends()) {
            if (users.contains(friend)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            List<User> road = new ArrayList<>();
            road.add(source);
            return road;
        } else {
            List<User> copy = new ArrayList<>(users);
            copy.remove(source);
            List<User> maxRoad = new ArrayList<>();
            for (User friend :
                    source.getFriends()) {
                if (longestRoad(copy, friend).size() > maxRoad.size() && copy.contains(friend)) {
                    maxRoad = longestRoad(copy, friend);
                    maxRoad.add(source);
                }
            }
            return maxRoad;
        }
    }

    public User findUserPass(String username, String password){
        return userRepository.findUserPass(username,password);
    }

    public List<UserFriendshipDTO> getFriendsOf(User loggedInUser){
        List<UserFriendshipDTO> friendshipDTOS = new ArrayList<>();
        List<Friendship> friendships = friendshipRepository.getAll();
        for (Friendship friendship :
                friendships) {
            int senderID = friendship.getUser1();
            int receiverID = friendship.getUser2();
            int loggedID = loggedInUser.getID();
            if(loggedID==senderID){
                String username = userRepository.findID(receiverID).getUsername();
                LocalDateTime friendsFrom = friendship.getFriendsFrom();
                FriendshipStatus friendshipStatus = friendship.getStatus();

                friendshipDTOS.add(new UserFriendshipDTO(senderID, receiverID, username,friendsFrom,friendshipStatus));
            }
            if(loggedID==receiverID){
                String username = userRepository.findID(senderID).getUsername();
                LocalDateTime friendsFrom = friendship.getFriendsFrom();
                FriendshipStatus friendshipStatus = friendship.getStatus();

                friendshipDTOS.add(new UserFriendshipDTO(senderID, receiverID, username,friendsFrom,friendshipStatus));
            }

        }
        return friendshipDTOS;
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer:
        observers){
            observer.update();
        }
    }

    public Iterable<User> getAllUsers() {
        return userRepository.getAll();
    }

    public void removeFriend(String username1, String username2) throws ServiceException {
        User user1 = userRepository.findUsername(username1);
        User user2 = userRepository.findUsername(username2);
        int id1 = user1.getID();
        int id2 = user2.getID();
        removeFriend(id1,id2);
    }

    public void acceptFriend(String username1, String username2) throws ServiceException {
        User user1 = userRepository.findUsername(username1);
        User user2 = userRepository.findUsername(username2);
        int id1 = user1.getID();
        int id2 = user2.getID();
        removeFriend(id1,id2);

        addAccFriend(id1,id2);
    }

    private void addAccFriend(int ID1, int ID2) throws ServiceException {
        if (userRepository.findID(ID1) == null || userRepository.findID(ID2) == null) {
            throw new ServiceException("User doesn't exist!");
        }
        if (ID1 != ID2) {
            User trueUser1 = userRepository.findID(ID1);
            User trueUser2 = userRepository.findID(ID2);
            List<User> users = userRepository.getAll();
            for (User user :
                    users) {
                if (user.getID() == ID1) {
                    user.addFriend(trueUser2);
                } else if (user.getID() == ID2) {
                    user.addFriend(trueUser1);
                    try {
                        if(friendshipRepository.findID(ID1,ID2)==null) {
                            friendshipRepository.add(new Friendship(ID1, ID2, LocalDateTime.now(), FriendshipStatus.ACCEPTED));
                            notifyObservers();
                        }
                    } catch (RepoException e) {
                        e.print();
                    } catch (ValidationException e) {
                        e.print();
                    }
                }
            }
        } else {
            throw new ServiceException("User can't be his own friend!");
        }
    }
}
