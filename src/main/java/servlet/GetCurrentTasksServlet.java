package servlet;

import postgres.ListItemOperations;
import postgres.ToDoListOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Ratiu on 11/04/2015.
 */
public class GetCurrentTasksServlet extends HttpServlet{
    public static final String GET_TASKS_PARAMETER = "getCurrentTasks";
    public static final String PARENT_LIST_PARAMETER = "parentListName";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(true);
        int userId = (Integer)session.getAttribute(LoginServlet.USER_ID_PARAMETER);

        int parentListId;
        // Getting all active lists from the database
        List<String> currentTasks = new ArrayList<String>();
        if (Boolean.valueOf(request.getParameter(GET_TASKS_PARAMETER))) {
            try {
                parentListId = ToDoListOperations.connectAndGetListId(userId, request.getParameter(PARENT_LIST_PARAMETER));
                currentTasks = ListItemOperations.getCurrentTasks(parentListId);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(400, "SQL errors encountered.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.sendError(400, "Failed to load jdbc driver.");
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }

        // Building the json object containing the names of all active lists
        int tasksCount = currentTasks.size();
        String jsonObject = "{\"tasks\": [";
        if (tasksCount > 0) {

            for (int i = 0; i < tasksCount; i++) {
                jsonObject += "\"" + currentTasks.get(i) + "\"";
                if (i < tasksCount - 1) {
                    jsonObject += ", ";
                }
            }
            jsonObject += "]}";
        } else {
            jsonObject = "{\"noActiveTasks\":true}";
        }

        // Sending over the json object
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
    }
}
