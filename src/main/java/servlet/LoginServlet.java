package servlet;

import postgres.ListItemOperations;
import postgres.LoginOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 18/04/2015.
 */
public class LoginServlet extends HttpServlet {

    public static final String USERNAME_PARAMETER = "username";
    public static final String USER_ID_PARAMETER = "userId";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        String username = request.getParameter(USERNAME_PARAMETER);
        System.out.println("Username " + username);

        int currentUserId = 0;
        try {
            currentUserId = LoginOperations.getUserId(username);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String jsonObject = "{\"userId\":";
        if (currentUserId > 0) {
            session.setAttribute(USER_ID_PARAMETER, currentUserId);
            jsonObject += +currentUserId + "}";
        } else {
            jsonObject = "{\"userNotFound\":true}";
        }

        // Sending over the json object
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
    }

}
