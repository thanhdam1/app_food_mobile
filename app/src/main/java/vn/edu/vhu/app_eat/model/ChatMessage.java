package vn.edu.vhu.app_eat.model;

public class ChatMessage {
    private String message;
    private boolean isFromUser;
    private String time;

    public ChatMessage(String message, boolean isFromUser, String time) {
        this.message = message;
        this.isFromUser = isFromUser;
        this.time = time;
    }

    public String getMessage() { return message; }
    public boolean isFromUser() { return isFromUser; }
    public String getTime() { return time; }
}
