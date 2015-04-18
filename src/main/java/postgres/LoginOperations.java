package postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 18/04/2015.
 */
public class LoginOperations {

    public static final String TABLE_NAME = "users";
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";

    public static int getUserId(String username) throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        // Getting the list id
        PreparedStatement getUserIdQuery = connection.prepareStatement("select " + ID_COLUMN + " from " + TABLE_NAME +
                        " where " + NAME_COLUMN + " = ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        getUserIdQuery.setString(1, username);
        ResultSet resultSet = getUserIdQuery.executeQuery();
        resultSet.next();
        int userId = resultSet.getInt(ID_COLUMN);
        getUserIdQuery.close();
        resultSet.close();
        connection.close();

        return userId;
    }
}
