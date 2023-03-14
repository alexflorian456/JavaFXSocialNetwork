package DTO;

public class MessageDTO {
    private String username;
    private String content;

    public MessageDTO(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}
