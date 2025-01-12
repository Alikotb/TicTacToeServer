package tictactoeserver.model;

public class User {
    private String username;
    private String email;
    private String password;
    private int score;
    private boolean isOnline;
    private boolean isAvailable;

    public User(String username, String email, String password, int score, boolean isOnline, boolean isAvailable) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = score;
        this.isOnline = isOnline;
        this.isAvailable = isAvailable;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", email=" + email + ", password=" + password + ", score=" + score + ", isOnline=" + isOnline + ", isAvailable=" + isAvailable + '}';
    }
}
