package logic.service;

import com.google.common.collect.Iterables;
import logic.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserService {

    private Session session;

    public UserService() {
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
        if (q.getResultList().size() == 1)
            return Optional.of(Iterables.getOnlyElement(q.getResultList()));
        else
            return Optional.empty();
    }
}
