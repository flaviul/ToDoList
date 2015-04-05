package postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 05/04/2015.
 */
public class ToDoListOperations {

    public static final String TABLE_NAME = "to_do_list";
    public static final String ID_COLUMN = "list_id";
    public static final String NAME_COLUMN = "list_name";

    public static boolean addList(String listName) throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("insert into " + TABLE_NAME + " (" + NAME_COLUMN + ") values (?);");
        preparedStatement.setString(1, listName);

        int insertedRows = preparedStatement.executeUpdate();
        boolean successfulOperation = false;

        if (insertedRows > 0) {
            successfulOperation = true;
        }

        preparedStatement.close();
        connection.close();

        return successfulOperation;
    }


}
