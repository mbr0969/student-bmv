package edu.bmv.studentorder.dao;

import edu.bmv.studentorder.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBuilder  {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_USER),
                Config.getProperty(Config.DB_PASSWORD));
    }

}
