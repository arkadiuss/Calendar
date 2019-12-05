package logic.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class HibernateProvider {

    private static SessionFactory factory;
    private static Session session;
    public static Session getSession(){
        if(factory == null){
            Configuration configuration = new Configuration();
            factory = configuration.configure().buildSessionFactory();
            session = factory.openSession();
        }
        return session;
    }
}
