package todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todo.model.Task;
import todo.model.User;

import java.util.List;
import java.util.function.Function;

public class ToDoStore implements Store {
    private final SessionFactory sf;

    private ToDoStore() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private static final class Lazy {
        private static final Store INST = new ToDoStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    private <T> T tx(final Function<Session, T> command) {
        T rsl;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            rsl = command.apply(session);
            session.getTransaction().commit();
        }
        return rsl;
    }

    @Override
    public void addTask(Task task) {
        tx(session -> session.save(task));
    }

    @Override
    public List<Task> getAllTask() {
        return tx(session -> session.createQuery("from Task").list());
    }

    @Override
    public List<Task> getActualTask() {
        return tx(session -> session.createQuery("from Task where done = false").list());
    }

    @Override
    public void updateTask(int id) {
        tx(session ->
                session.createQuery("UPDATE Task SET done = true WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate());
    }

    public void addUser(User user) {
        tx(session -> session.save(user));
    }

    @Override
    public User findUserByEmail(String email) {
        List<User> list = tx(session -> session.createQuery("from User where email = :email")
                .setParameter("email", email)
                .list());
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public User findUserById(int id) {
        return tx(session -> session.get(User.class, id));
    }
}
