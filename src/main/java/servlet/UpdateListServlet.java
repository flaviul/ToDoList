package servlet;

import postgres.ToDoListOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 11/04/2015.
 */
public class UpdateListServlet extends HttpServlet {
    public static final String LIST_NAME_PARAMETER = "listName";
    public static final String ACTION_PARAMETER = "action";
    public static final int MARK_DONE_ACTION = 0;
    public static final int DELETE_LIST_ACTION = -1;
    public static final int CHANGE_CONTENT_ACTION = 1;
    public static final String NEW_CONTENT_PARAMETER = "newListName";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listName = request.getParameter(LIST_NAME_PARAMETER);
        int requestedAction = Integer.parseInt(request.getParameter(ACTION_PARAMETER));

        switch (requestedAction) {
            case MARK_DONE_ACTION:
                try {
                    if (!ToDoListOperations.markListDone(listName)) {
                        response.sendError(400, "Failed to mark task as done.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendError(400, "Failed to mark task as done. SQL errors encountered.");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(400, "Failed to mark task as done. Could not load jdbc driver.");
                }
                break;

            case DELETE_LIST_ACTION:
                // TODO: add delete functionality
                break;

            case CHANGE_CONTENT_ACTION:
                // TODO: add edit content functionality
                break;
        }

    }
}
