package postgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Ratiu on 05/04/2015.
 */
public class ListItemOperations {

    private static final String TABLE_NAME = "list_items";
    private static final String LIST_ID_COLUMN = "list_id";
    private static final String ITEM_CONTENT_COLUMN = "item_content";
    private static final String CREATED_AT_COLUMN = "created_at";
    private static final String STATUS_COLUMN = "done";
    private static final String DONE_AT_COLUMN = "done_at";


    public static boolean addItem(String listName, String itemContent) throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        // Getting the list id
        PreparedStatement listIdQuery = connection.prepareStatement("select " + ToDoListOperations.ID_COLUMN + " from " + ToDoListOperations.TABLE_NAME +
                        " where " + ToDoListOperations.NAME_COLUMN + " = ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        listIdQuery.setString(1, listName);
        ResultSet resultSet = listIdQuery.executeQuery();
        resultSet.next();
        int listId = resultSet.getInt(ToDoListOperations.ID_COLUMN);
        listIdQuery.close();
        resultSet.close();


        // Preparing statement
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into " + TABLE_NAME + " (" + LIST_ID_COLUMN + ", " + ITEM_CONTENT_COLUMN + ", "
                        + CREATED_AT_COLUMN + ") values (?, ?, ?);"
        );
        preparedStatement.setInt(1, listId);
        preparedStatement.setString(2, itemContent);

        // Getting the current date in java.sql.Date format
        java.util.Date utilDate = new java.util.Date();
        Date sqlDate = new Date(utilDate.getTime());
        preparedStatement.setDate(3, sqlDate);

        int insertedRows = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();

        boolean successfulOperation = false;
        if (insertedRows > 0) {
            successfulOperation = true;
        }
        return successfulOperation;
    }

    public static List<String> getCurrentTasks(int parentListId) throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select " + ITEM_CONTENT_COLUMN + " from " + TABLE_NAME
                        + " where " + LIST_ID_COLUMN + " = ? and " + STATUS_COLUMN + " != true;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setInt(1, parentListId);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> currentTasks = new ArrayList<String>();
        while (resultSet.next()){
            currentTasks.add(resultSet.getString(ITEM_CONTENT_COLUMN));
        }
        return currentTasks;
    }

}
