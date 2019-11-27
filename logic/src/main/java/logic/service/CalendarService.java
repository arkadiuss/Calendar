package logic.service;

import logic.model.Calendar;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class CalendarService {

    private Session session;

    public CalendarService() {
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
}
