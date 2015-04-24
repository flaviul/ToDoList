package postgres;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 18/04/2015.
 */
public class SignUpOperations {

    public static final String TABLE_NAME = "users";
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";

    public static int insertUser(String username) throws SQLException, ClassNotFoundException, PropertyVetoException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        // Getting the list id
        PreparedStatement insertUserQuery = connection.prepareStatement("insert into " + TABLE_NAME + " (" + NAME_COLUMN + ") values (?);");
        insertUserQuery.setString(1, username);
        int insertedRows = insertUserQuery.executeUpdate();

        int userId = 0;
        if (insertedRows > 0) {
            PreparedStatement getUserIdQuery = connection.prepareStatement("select " + ID_COLUMN + " from " + TABLE_NAME +
                            " where " + NAME_COLUMN + " = ?;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            getUserIdQuery.setString(1, username);
            ResultSet resultSet = getUserIdQuery.executeQuery();
            resultSet.next();
            userId = resultSet.getInt(ID_COLUMN);
            getUserIdQuery.close();
            resultSet.close();
        }
        connection.close();
        return userId;
    }

}
