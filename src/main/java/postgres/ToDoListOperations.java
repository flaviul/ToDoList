package postgres;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Ratiu on 05/04/2015.
 */
public class ToDoListOperations {

    public static final String TABLE_NAME = "to_do_list";
    public static final String USER_ID_COLUMN = "user_id";
    public static final String ID_COLUMN = "list_id";
    public static final String NAME_COLUMN = "list_name";
    public static final String CREATED_AT_COLUMN = "created_at";
    public static final String STATUS_COLUMN = "done";
    public static final String DONE_AT_COLUMN = "done_at";

    public static boolean addList(int userId, String listName) throws SQLException, ClassNotFoundException, PropertyVetoException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("insert into " + TABLE_NAME + " (" + NAME_COLUMN + ", " + CREATED_AT_COLUMN + ", " + USER_ID_COLUMN +") values (?, ?, ?);");
        preparedStatement.setString(1, listName);
        // Getting the current date in java.sql.Date format
        java.util.Date utilDate = new java.util.Date();
        Object sqlDate = new Timestamp(utilDate.getTime());
        preparedStatement.setObject(2, sqlDate);
        preparedStatement.setInt(3, userId);

        int insertedRows = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();

        boolean successfulOperation = false;
        if (insertedRows > 0) {
            successfulOperation = true;
        }
        return successfulOperation;
    }

    public static List<String> activeLists(int userId) throws SQLException, ClassNotFoundException, PropertyVetoException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select " + NAME_COLUMN + " from " + TABLE_NAME + " where " + USER_ID_COLUMN + " = ? and " + STATUS_COLUMN + " = ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setInt(1, userId);
        preparedStatement.setBoolean(2, false);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> activeLists = new ArrayList<String>();
        while (resultSet.next()){
            activeLists.add(resultSet.getString(NAME_COLUMN));
        }

        preparedStatement.close();
        resultSet.close();
        connection.close();

        return activeLists;
    }

    public static int getListId(int userId, String listName, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select " + ID_COLUMN + " from " + TABLE_NAME + " where " + NAME_COLUMN + " = ? and " + USER_ID_COLUMN + "= ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setString(1, listName);
        preparedStatement.setInt(2, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        int listId = 0;
        if (resultSet.next()){
            listId = resultSet.getInt(ID_COLUMN);
        }

        preparedStatement.close();
        resultSet.close();

        return listId;
    }

    public static int connectAndGetListId(int userId, String listName) throws SQLException, ClassNotFoundException, PropertyVetoException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();
        int listId = getListId(userId, listName, connection);
        connection.close();
        return listId;
    }

    public static String getLatestList(int userId) throws SQLException, ClassNotFoundException, PropertyVetoException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select " + NAME_COLUMN + " from " + TABLE_NAME + " where " +
                        USER_ID_COLUMN + " = ? and " + STATUS_COLUMN + " = ? order by " + CREATED_AT_COLUMN + " desc limit 1;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setInt(1, userId);
        preparedStatement.setBoolean(2, false);
        ResultSet resultSet = preparedStatement.executeQuery();

        String latestList = null;
        if (resultSet.next()){
            latestList = resultSet.getString(NAME_COLUMN);
        }

        preparedStatement.close();
        resultSet.close();
        connection.close();

        return latestList;
    }

    public static boolean markListDone(int userId, String listName) throws SQLException, ClassNotFoundException, PropertyVetoException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        int listId = ToDoListOperations.getListId(userId, listName, connection);

        PreparedStatement updateList = connection.prepareStatement("update " + TABLE_NAME + " set " + STATUS_COLUMN + " = ?" + ", " +
                DONE_AT_COLUMN + " = ? where " + ID_COLUMN + " = ?;");
        updateList.setBoolean(1, true);
        // Getting the current date in java.sql.Date format
        java.util.Date utilDate = new java.util.Date();
        Object sqlDate = new Timestamp(utilDate.getTime());
        updateList.setObject(2, sqlDate);
        updateList.setInt(3, listId);

        int updatedRows = updateList.executeUpdate();
        updateList.close();

        // Marking all tasks in the list as done
        PreparedStatement updateTasks = connection.prepareStatement("update " + ListItemOperations.TABLE_NAME + " set " + ListItemOperations.STATUS_COLUMN + " = ?" + ", " +
                ListItemOperations.DONE_AT_COLUMN + " = ? where " + ListItemOperations.LIST_ID_COLUMN + " = ? and "+ ListItemOperations.STATUS_COLUMN + " != ?;");
        updateTasks.setBoolean(1, true);
        updateTasks.setObject(2, sqlDate);
        updateTasks.setInt(3, listId);
        updateTasks.setBoolean(4, true);

        int updatedTasks = updateTasks.executeUpdate();
        updateTasks.close();

        connection.close();

        boolean successfulOperation = false;
        if (updatedRows > 0) {
            successfulOperation = true;
        }
        return successfulOperation;
    }

}
