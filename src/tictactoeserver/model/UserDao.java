package tictactoeserver.model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

public class UserDao {

    private static Connection con;
    private static PreparedStatement stmt;
    private static ResultSet result;
    private static volatile UserDao instance;
    
    private UserDao(){}
    
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean signup(User user) {
        try {
            String query = "INSERT INTO USER_TABLE (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)";
            stmt = con.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            if (stmt.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
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

}
