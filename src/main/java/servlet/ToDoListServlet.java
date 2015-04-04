package servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Flaviu Ratiu on 04/04/2015.
 */
public class ToDoListServlet extends HttpServlet {
    public static final String NEW_TASK_VALUE_PARAMETER = "newTask";

    public void service(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("To do");
        String myValue = request.getParameter(NEW_TASK_VALUE_PARAMETER);
        System.out.println("Value " + myValue);
    }
}
