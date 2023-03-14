package DTO;

import Domain.FriendshipStatus;

import java.time.LocalDateTime;

public class UserFriendshipDTO {
    private int senderID;
    private int receiverID;
    private String username;
    private LocalDateTime friendsFrom;
    private FriendshipStatus status;

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public UserFriendshipDTO(int senderID, int receiverID, String username, LocalDateTime friendsFrom, FriendshipStatus status) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.username = username;
        this.friendsFrom = friendsFrom;
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public FriendshipStatus getStatus() {
        return status;
    }
}
