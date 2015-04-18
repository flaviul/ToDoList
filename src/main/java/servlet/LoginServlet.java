package servlet;

import postgres.ListItemOperations;
import postgres.LoginOperations;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by Flaviu Ratiu on 18/04/2015.
 */
public class LoginServlet extends HttpServlet{

    public static final String USERNAME_PARAMETER = "username";

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter(USERNAME_PARAMETER);
        System.out.println("Username " + username);

        int currentUserId;
        try {
            currentUserId = LoginOperations.getUserId(username);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
