package logic.service;

import logic.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class UserService {

    private Session session;

    public UserService() {
        session = HibernateProvider.getSession();
    }

    public void addUser(User user){
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.persist(user);
        transaction.commit();
    }

    public List<User> getUsers(){
        Query q = session.createQuery("from User");
        return q.getResultList();
    }
}
