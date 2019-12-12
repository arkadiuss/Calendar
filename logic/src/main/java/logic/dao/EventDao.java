package logic.dao;

import logic.model.Event;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EventDao {

    private Session session;

    public EventDao() {
        session = HibernateProvider.getSession();
    }

    public void updateEvent(Event event) {
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.update(event);
        transaction.commit();
    }
}
