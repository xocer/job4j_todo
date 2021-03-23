package todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todo.model.Task;

import java.util.List;

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

    @Override
    public void addItem(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Task> getAllItem() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> list = session.createQuery("from Task").list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    @Override
    public List<Task> getActualItem() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> list = session.createQuery("from Task where done = false").list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    @Override
    public void updateItem(int test) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("UPDATE Task SET done = true WHERE id = :id").
                setParameter("id", test).
                executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
