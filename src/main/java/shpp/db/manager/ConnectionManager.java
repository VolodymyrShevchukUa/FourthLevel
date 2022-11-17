package shpp.db.manager;

import shpp.db.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    Config config;
    Connection connection;

    public Connection getConnection() {
        config = new Config();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
           connection =  DriverManager.getConnection(config.getURL(),config.getNAME(),config.getPASS());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
