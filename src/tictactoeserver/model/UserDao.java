package tictactoeserver.model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

public class UserDao {

    private static Connection con;
    private static PreparedStatement stmt;
    private static ResultSet result;

    static {
        try {
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/Users", "root", "root");
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String signup(User user) throws SQLException {
        String query = "INSERT INTO USER_TABLE (USER_NAME, EMAIL, PASSWORD) VALUES (?, ?, ?)";
        stmt = con.prepareStatement(query);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPassword());
        if (stmt.executeUpdate() > 0) {
            return "success, Signup successful";
        }
        return "error, Username already exists.";
    }

}
