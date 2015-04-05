package servlet;

import postgres.ListItemOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 04/04/2015.
 */
public class AddItemServlet extends HttpServlet {
    public static final String LIST_NAME_PARAMETER = "listName";
    public static final String NEW_TASK_VALUE_PARAMETER = "newTask";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listName = request.getParameter(LIST_NAME_PARAMETER);
        String newTask = request.getParameter(NEW_TASK_VALUE_PARAMETER);
        System.out.println("List name " + listName);
        System.out.println("Value " + newTask);

        try {
            if (!ListItemOperations.addItem(listName, newTask)){
                response.sendError(400, "Failed to add task to the list.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(400, "Failed to add task to the list.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(400, "Failed to add task to the list.");
        }

    }
}
