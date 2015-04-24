package postgres;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 05/04/2015.
 */
public class PostgresConnection {

    private Connection connection;

    public PostgresConnection() throws SQLException, ClassNotFoundException, PropertyVetoException {
//        Class.forName("org.postgresql.Driver");
//
//        final String URL = "jdbc:postgresql://54.93.65.5:5432/dragon_to_do_list_db";
//        final String USERNAME = "fasttrackit_dev";
//        final String PASSWORD = "fasttrackit_dev";
//
//        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "org.postgresql.Driver" );
        cpds.setJdbcUrl( "jdbc:postgresql://54.93.65.5:5432/dragon_to_do_list_db" );
        cpds.setUser("fasttrackit_dev");
        cpds.setPassword("fasttrackit_dev");

        // the settings below are optional -- c3p0 can work with defaults
//        cpds.setMinPoolSize(5);
//        cpds.setAcquireIncrement(5);
//        cpds.setMaxPoolSize(20);
        connection = cpds.getConnection();
    }

    public Connection getConnection() {
        return connection;
    }
}
