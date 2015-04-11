package postgres;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Ratiu on 05/04/2015.
 */
public class ToDoListOperations {

    public static final String TABLE_NAME = "to_do_list";
    public static final String ID_COLUMN = "list_id";
    public static final String NAME_COLUMN = "list_name";
    public static final String CREATED_AT_COLUMN = "created_at";

    public static boolean addList(String listName) throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("insert into " + TABLE_NAME + " (" + NAME_COLUMN + ", " + CREATED_AT_COLUMN + ") values (?, ?);");
        preparedStatement.setString(1, listName);
        // Getting the current date in java.sql.Date format
        java.util.Date utilDate = new java.util.Date();
        Object sqlDate = new Timestamp(utilDate.getTime());
        preparedStatement.setObject(2, sqlDate);

        int insertedRows = preparedStatement.executeUpdate();
        boolean successfulOperation = false;

        if (insertedRows > 0) {
            successfulOperation = true;
        }

        preparedStatement.close();
        connection.close();

        return successfulOperation;
    }

    public static List<String> activeLists() throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select " + NAME_COLUMN + " from " + TABLE_NAME + ";",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> activeLists = new ArrayList<String>();
        while (resultSet.next()){
            activeLists.add(resultSet.getString(NAME_COLUMN));
        }
        return activeLists;
    }

    public static int getListId(String list_name) throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select " + ID_COLUMN + " from " + TABLE_NAME + " where " + NAME_COLUMN + " = ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setString(1, list_name);
        ResultSet resultSet = preparedStatement.executeQuery();

        int listId = 0;
        if (resultSet.next()){
            listId = resultSet.getInt(ID_COLUMN);
        }
        return listId;
    }

    public static String getLatestList() throws SQLException, ClassNotFoundException {
        PostgresConnection postgres = new PostgresConnection();
        Connection connection = postgres.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select " + NAME_COLUMN + " from " + TABLE_NAME + " order by " + CREATED_AT_COLUMN + " desc limit 1;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = preparedStatement.executeQuery();

        String latestList = null;
        if (resultSet.next()){
            latestList = resultSet.getString(NAME_COLUMN);
        }
        return latestList;
    }


}
