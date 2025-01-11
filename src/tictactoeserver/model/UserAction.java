package tictactoeserver.model;

public class UserAction {
    private int action;
    private User user;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
