package servlet;

import postgres.ListItemOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 04/04/2015.
 */
public class AddItemServlet extends HttpServlet {
    public static final String LIST_NAME_PARAMETER = "listName";
    public static final String NEW_TASK_VALUE_PARAMETER = "newTask";
    public static final String DUPLICATE_ERROR_PARAMETER = "duplicateError";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listName = request.getParameter(LIST_NAME_PARAMETER);
        String newTask = request.getParameter(NEW_TASK_VALUE_PARAMETER);
        System.out.println("List name " + listName);
        System.out.println("Value " + newTask);

        try {
            ListItemOperations.addItem(listName, newTask);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.toString().toLowerCase().contains("duplicate key value violates unique constraint")) {
                String jsonResponse = "{" + DUPLICATE_ERROR_PARAMETER + ": true}";
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse);
                out.flush();
            } else {
                response.sendError(400, "Failed to add task. SQL errors encountered.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(400, "Failed to add task. Could not load jdbc driver.");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

    }
}
