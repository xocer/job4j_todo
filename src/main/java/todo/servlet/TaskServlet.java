package todo.servlet;

import com.alibaba.fastjson.JSON;
import todo.model.Task;
import todo.store.ToDoStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class TaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String desc = req.getParameter("desc");
        req.setCharacterEncoding("UTF-8");
        ToDoStore.instOf().addItem(new Task(desc, LocalDateTime.now(), false));
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "all" -> {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(JSON.toJSONString(ToDoStore.instOf().getAllItem()));
            }
            case "actual" -> {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(JSON.toJSONString(ToDoStore.instOf().getActualItem()));
            }
            case "update" -> {
                ToDoStore.instOf().updateItem(Integer.parseInt(req.getParameter("id")));
            }
            default -> resp.sendRedirect(req.getContextPath() + "/index.html");
        }
    }
}
