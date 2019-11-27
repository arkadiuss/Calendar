package logic.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class HibernateProvider {

    private static SessionFactory factory;

    public static Session getSession(){
        if(factory == null){
            Configuration configuration = new Configuration();
            factory = configuration.configure().buildSessionFactory();
        }
        return factory.openSession();
    }
}
