package logic.dao;

import logic.model.Calendar;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class CalendarDao {

    private Session session;

    public CalendarDao() {
        session = HibernateProvider.getSession();
    }

    public void addCalendar(Calendar calendar){
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.persist(calendar);
        transaction.commit();

    }

    public List<Calendar> getCalendars(){
        Query q = session.createQuery("from Calendar");
        return q.getResultList();
    }

    public void deleteCalendar(Calendar calendar){
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.delete(calendar);
        transaction.commit();

    }

    public void updateCalendar(Calendar calendar) {
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.update(calendar);
        transaction.commit();
    }
}
