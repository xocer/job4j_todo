package todo.store;

import todo.model.Task;

import java.util.List;

public interface Store {
    void addItem(Task task);

    List<Task> getAllItem();

    List<Task> getActualItem();

    void updateItem(int id);
}
