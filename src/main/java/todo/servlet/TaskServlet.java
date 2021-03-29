package todo.servlet;

import com.alibaba.fastjson.JSON;
import todo.model.Category;
import todo.model.Task;
import todo.model.User;
import todo.store.ToDoStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String desc = req.getParameter("desc");
        req.setCharacterEncoding("UTF-8");
        List<String> category = JSON.parseArray(req.getParameter("cat")).toJavaList(String.class);
        User user = ToDoStore.instOf().findUserById(Integer.parseInt(req.getParameter("id")));
        final var task = new Task();
        task.setDescription(desc);
        task.setCreated(LocalDateTime.now());
        task.setUser(user);
        category.forEach(cat -> task.getCategories().add(Category.of(cat)));
        ToDoStore.instOf().addTask(task);
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "all" -> {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(JSON.toJSONString(ToDoStore.instOf().getAllTask()));
            }
            case "actual" -> {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(JSON.toJSONString(ToDoStore.instOf().getActualTask()));
            }
            case "update" -> {
                ToDoStore.instOf().updateTask(Integer.parseInt(req.getParameter("id")));
            }
            default -> resp.sendRedirect(req.getContextPath() + "/index.html");
        }
    }
}
