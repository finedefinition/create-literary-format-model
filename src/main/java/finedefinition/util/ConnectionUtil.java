package finedefinition.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can`t load JDBC driver for MySQL", e);
        }
    }

    public static Connection getConnection() {
        try {
            Properties dbProperties = new Properties();
            dbProperties.put("user", "root");
            dbProperties.put("password", "3094410014");
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/library_db", dbProperties);

        } catch (SQLException throwable) {
            throw new RuntimeException("Can`t create connection to DB", throwable);
        }
    }
}
