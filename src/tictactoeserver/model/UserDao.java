package tictactoeserver.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

public class UserDao {

    private static Connection con;
    private static PreparedStatement stmt;
    private static ResultSet result;
    private static volatile UserDao instance;

    private UserDao() {
    }

    public static UserDao getInstance() {
        if (instance == null) {
            synchronized (UserDao.class) {
                if (instance == null) {
                    instance = new UserDao();
                }
            }
        }
        return instance;
    }

    static {

        try {
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/tictactoe_db", "root", "root");

        } catch (SQLException ex) {
            System.out.println("Disconnection with Database.");
        }
    }

    public String signup(User user) {
        try {
            String checkQuery = "SELECT EMAIL, USERNAME FROM USER_TABLE WHERE EMAIL = ? OR USERNAME = ?";
            stmt = con.prepareStatement(checkQuery);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String existingEmail = rs.getString("EMAIL");
                String existingUsername = rs.getString("USERNAME");

                if (existingUsername != null && existingUsername.equalsIgnoreCase(user.getUsername())) {
                    return "Username already exists.";
                }
                
                if (existingEmail != null && existingEmail.equalsIgnoreCase(user.getEmail())) {
                    return "Email already exists.";
                }
                
            }

            String insertQuery = "INSERT INTO USER_TABLE (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)";
            stmt = con.prepareStatement(insertQuery);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());

            if (stmt.executeUpdate() > 0) {
                return "success";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Server error occurred during signup.";
        }
        return "Signup failed for unknown reasons.";
    }

    public User updateUser(User user) {
        try {
            if (user != null) {
                user.setIsOnline(true);
                user.setIsAvailable(true);
                String updateQuery = "UPDATE USER_TABLE SET ISONLINE = ?, ISAVAILABLE = ? WHERE EMAIL = ?";
                stmt = con.prepareStatement(updateQuery);
                stmt.setBoolean(1, true);
                stmt.setBoolean(2, true);
                stmt.setString(3, user.getEmail());
                stmt.executeUpdate();
                return user;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private User getUser(User user) throws SQLException {
        String loginQuery = "SELECT USERNAME, PASSWORD, SCORE FROM USER_TABLE WHERE EMAIL = ? AND PASSWORD = ?";
        stmt = con.prepareStatement(loginQuery);
        stmt.setString(1, user.getEmail());
        stmt.setString(2, user.getPassword());
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            return user;
        }
        return null;
    }

    public String getUserNameByEmail(String email) throws SQLException {
        String loginQuery = "SELECT USERNAME FROM USER_TABLE WHERE EMAIL = ? ";
        stmt = con.prepareStatement(loginQuery);
        stmt.setString(1, email);
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            return result.getString(1);

        }
        return null;
    }

    public static int[] getOnlineUsers() {
        int[] counter = {0, 0};
        if (con == null) {
            System.out.println("Don't get Online Users,Database is Disconnection");
        } else {
            try {
                stmt = con.prepareStatement("SELECT COUNT(*) FROM USER_TABLE WHERE ISONLINE = TRUE");
                result = stmt.executeQuery();
                if (result.next()) {
                    counter[0] = result.getInt(1);
                }
                stmt = con.prepareStatement("SELECT COUNT(*) FROM USER_TABLE WHERE ISONLINE = FALSE");
                result = stmt.executeQuery();
                if (result.next()) {
                    counter[1] = result.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return counter;
    }

    public ArrayList<User> getAvailableUsers() {
        ArrayList<User> users = new ArrayList();
        User user;
        if (con == null) {
            System.out.println("Don't get Available Users,Database is Disconnection");
        } else {
            try {
                stmt = con.prepareStatement("SELECT USERNAME,SCORE FROM USER_TABLE WHERE ISAVAILABLE = TRUE");
                result = stmt.executeQuery();
                while (result.next()) {
                    user = new User(result.getString("USERNAME"), result.getInt("SCORE"));
                    users.add(user);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return users;
    }

    public int getScoreByEmail(String email) throws SQLException {
        String loginQuery = "SELECT SCORE FROM USER_TABLE WHERE EMAIL = ? ";
        stmt = con.prepareStatement(loginQuery);
        stmt.setString(1, email);
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            return result.getInt("SCORE");

        }
        return 0;
    }

    public String checkEmail(String email) throws SQLException {
        String loginQuery = "SELECT EMAIL FROM USER_TABLE WHERE EMAIL = ? ";
        stmt = con.prepareStatement(loginQuery);
        stmt.setString(1, email);
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            return result.getString(1);

        }
        return null;
    }

    public String checkPassword(String email) throws SQLException {
        String loginQuery = "SELECT PASSWORD FROM USER_TABLE WHERE EMAIL = ? ";
        stmt = con.prepareStatement(loginQuery);
        stmt.setString(1, email);
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            return result.getString("PASSWORD");

        }
        return null;
    }

    public boolean updateScore(String username, int newScore) throws SQLException {
        String query = "UPDATE USER_TABLE SET SCORE = ? WHERE USERNAME = ?";
        stmt = con.prepareStatement(query);
        stmt.setInt(1, newScore);
        stmt.setString(2, username);
        if (stmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void logOut(User user) {
        try {
            String updateQuery = "UPDATE USER_TABLE SET ISONLINE = ?, ISAVAILABLE = ? WHERE USERNAME = ?";
            stmt = con.prepareStatement(updateQuery);
            stmt.setBoolean(1, false);
            stmt.setBoolean(2, false);
            stmt.setString(3, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isAvailable(String username) {
        try {
            String query = "SELECT ISAVAILABLE FROM USER_TABLE WHERE USERNAME = ? AND ISAVAILABLE = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setBoolean(2, true);
            ResultSet res = stmt.executeQuery();
            return res.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "UserName Not Available", ex);
            return false;
        }

    }

    public boolean isOnline(String username) {
        try {
            String query = "SELECT ISONLINE FROM USER_TABLE WHERE USERNAME = ? AND ISONLINE = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setBoolean(2, true);
            ResultSet res = stmt.executeQuery();
            return res.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "UserName Not Online", ex);
            return false;
        }
    }

    public boolean setIsNotAvailable(String username) {
        try {
            String query = "UPDATE USER_TABLE SET ISAVAILABLE = ? WHERE USERNAME = ?";
            stmt = con.prepareStatement(query);
            stmt.setBoolean(1, false);
            stmt.setString(2, username);
            int res = stmt.executeUpdate();
            return res > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, "isAvailable is Not Updated", ex);
            return false;
        }
    }

}
