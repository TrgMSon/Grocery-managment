package Connection;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DataConnection {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/database?user=root";
    private static final String user = "root";
    private static final String passWord = "020677MSon@";

    public static Connection setConnect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, passWord);
        } catch(SQLException e) {
            System.out.println("Error connect");
        }
        return conn;
    }
}