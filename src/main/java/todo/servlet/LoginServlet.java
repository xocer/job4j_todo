package todo.servlet;

import com.alibaba.fastjson.JSON;
import todo.store.ToDoStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        final var user = ToDoStore.instOf().findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            final var json = JSON.toJSONString(user);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
