package logic.dao;

import com.google.common.collect.Iterables;
import logic.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserDao {

    private Session session;

    public UserDao() {
        session = HibernateProvider.getSession();
    }

    public void addUser(User user) {
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.persist(user);
        transaction.commit();
    }

    public List<User> getUsers() {
        TypedQuery<User> q = session.createQuery("from User", User.class);
        return q.getResultList();
    }


    public Optional<User> getUser(String name, String password) {
        TypedQuery<User> q = session.createQuery("from User as u where u.username = :name" +
                " and u.password = :password", User.class);
        q.setParameter("name", name);
        q.setParameter("password", password);
        return Optional.ofNullable(q.getSingleResult());
    }

    public Optional<User> getUser(String name) {
        TypedQuery<User> q = session.createQuery("from User as u where u.username = :name", User.class);
        q.setParameter("name", name);
        return Optional.ofNullable(q.getSingleResult());
    }
}
