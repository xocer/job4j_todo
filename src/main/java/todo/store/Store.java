package todo.store;

import todo.model.Task;
import todo.model.User;

import java.util.List;

public interface Store {
    void addTask(Task task);

    List<Task> getAllTask();

    List<Task> getActualTask();

    void updateTask(int id);

    void addUser(User user);

    User findUserByEmail(String email);

    User findUserById(int id);
}
