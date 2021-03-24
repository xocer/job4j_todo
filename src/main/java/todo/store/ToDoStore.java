package todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todo.model.Task;

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
        Session session = sf.openSession();
        session.beginTransaction();
        T rsl = command.apply(session);
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    @Override
    public void addItem(Task task) {
        tx(session -> session.save(task));
    }

    @Override
    public List<Task> getAllItem() {
        return tx(session -> session.createQuery("from Task").list());
    }

    @Override
    public List<Task> getActualItem() {
        return tx(session -> session.createQuery("from Task where done = false").list());
    }

    @Override
    public void updateItem(int id) {
        tx(session ->
                session.createQuery("UPDATE Task SET done = true WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate());
    }
}
