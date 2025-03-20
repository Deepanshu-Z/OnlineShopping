import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databaseconnection {
    private static final String url = "jdbc:mysql://localhost:3306/?user=root";
    private static final String username = "root";
    private static final String password = "root";

    public static Connection getConnection() {
        System.out.println("Hello, World!");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}