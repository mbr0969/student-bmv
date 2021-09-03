package edu.bmv.city_register.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionBuilder {
    Connection getConnection() throws SQLException;
}
