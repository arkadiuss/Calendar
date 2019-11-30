package logic.service;

import logic.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

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
        Query q = session.createQuery("from User");
        return q.getResultList();
    }


    public User getUser(String name, String password) {
        TypedQuery<User> q = session.createQuery("from User as u where u.name = :name" +
                " and u.password = :password", User.class);
        q.setParameter("name", name);
        q.setParameter("password", password);
        return q.getSingleResult();
    }
}
