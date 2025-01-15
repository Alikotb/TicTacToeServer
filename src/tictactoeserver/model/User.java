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

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, boolean isOnline, boolean isAvailable) {
        this.username = username;
        this.isOnline = isOnline;
        this.isAvailable = isAvailable;
    }
    
    
    public User(String email,String password){
     this.email = email;
     this.password = password;
    }

    public User(String username, int score) {
        this.username = username;
        this.score = score;
    }
    

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
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
