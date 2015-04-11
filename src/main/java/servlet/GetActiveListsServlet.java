package servlet;

import postgres.ToDoListOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Ratiu on 06/04/2015.
 */
public class GetActiveListsServlet extends HttpServlet {
    public static final String GET_LISTS_PARAMETER = "getActiveLists";
    public static final String GET_LATEST_LIST_PARAMETER = "getLatestList";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Getting all active lists from the database
        List<String> activeLists = new ArrayList<String>();
        if (Boolean.valueOf(request.getParameter(GET_LISTS_PARAMETER))) {
            try {
                activeLists = ToDoListOperations.activeLists();
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(400, "SQL errors encountered.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.sendError(400, "Failed to load jdbc driver.");
            }
        }
        else if (Boolean.valueOf(request.getParameter(GET_LATEST_LIST_PARAMETER))){
            try {
                activeLists.clear();
                activeLists.add(ToDoListOperations.getLatestList());
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(400, "SQL errors encountered.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.sendError(400, "Failed to load jdbc driver.");
            }
        }

        // Building the json object containing the names of all active lists
        int listsCount = activeLists.size();
        String jsonObject = "{\"lists\": [";
        if (listsCount > 0) {

            for (int i = 0; i < listsCount; i++) {
                jsonObject += "\"" + activeLists.get(i) + "\"";
                if (i < listsCount - 1) {
                    jsonObject += ", ";
                }
            }
            jsonObject += "]}";
        } else {
            jsonObject = "{\"noActiveLists\":true}";
        }

        // Sending over the json object
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
    }
}
