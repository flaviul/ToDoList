package servlet;

import postgres.ListItemOperations;
import postgres.ToDoListOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 05/04/2015.
 */
public class AddListServlet extends HttpServlet{

    public static final String LIST_NAME_PARAMETER = "listName";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listName = request.getParameter(LIST_NAME_PARAMETER);
        System.out.println("List name " + listName);

        try {
            if (!ToDoListOperations.addList(listName)) {
                response.sendError(400, "Failed to add to-do list.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(400, "Failed to add to-do list. SQL errors encountered.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(400, "Failed to add to-do list. Could not load jdbc driver.");
        }

    }
}
