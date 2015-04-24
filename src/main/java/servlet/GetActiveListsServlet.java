package servlet;

import postgres.LoginOperations;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Flaviu Ratiu on 06/04/2015.
 */
public class GetActiveListsServlet extends HttpServlet {
    public static final String GET_LISTS_PARAMETER = "getActiveLists";
    public static final String GET_LATEST_LIST_PARAMETER = "getLatestList";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        int currentUserId = (Integer) session.getAttribute(LoginServlet.USER_ID_PARAMETER);
        String userName = "";
        try {
            userName = LoginOperations.getUsername(currentUserId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        // Getting all active lists from the database
        List<String> activeLists = new ArrayList<String>();
        List<String> latestList = new ArrayList<String>();
        if (Boolean.valueOf(request.getParameter(GET_LISTS_PARAMETER))) {
            try {
                activeLists = ToDoListOperations.activeLists(currentUserId);
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
        if (Boolean.valueOf(request.getParameter(GET_LATEST_LIST_PARAMETER))) {
            try {
                latestList.add(ToDoListOperations.getLatestList(currentUserId));
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
        int listsCount = activeLists.size();
        String jsonObject = "{\"userName\": \"" + userName + "\", ";
        if (listsCount > 0) {
            jsonObject += "\"lists\": [";
            for (int i = 0; i < listsCount; i++) {
                jsonObject += "\"" + activeLists.get(i) + "\"";
                if (i < listsCount - 1) {
                    jsonObject += ", ";
                }
            }
            jsonObject += "], \"latestList\": \"" + latestList.get(0) + "\"}";
        } else {
            jsonObject += "\"noActiveLists\":true}";
        }

        // Sending over the json object
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
    }
}
