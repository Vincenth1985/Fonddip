package be.infernalwhale.dao;

import be.infernalwhale.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static be.infernalwhale.config.Config.*;

public class DBConnector {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DB_URL,
                DB_USERNAME,
                DB_PASSWORD
        );
    }
}
