package logic.service;

import logic.HibernateProvider;
import logic.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
