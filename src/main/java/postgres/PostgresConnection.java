package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 05/04/2015.
 */
public class PostgresConnection {

    private Connection connection;

    public PostgresConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        final String URL = "jdbc:postgresql://54.93.65.5:5432/dragon_to_do_list_db";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }
}
