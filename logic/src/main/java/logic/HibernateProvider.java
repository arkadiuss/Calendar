package logic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class HibernateProvider {

    private static SessionFactory factory;

    public static Session getSession(){
        if(factory == null){
            Configuration configuration = new Configuration();
            factory = configuration.configure().buildSessionFactory();
        }
        return factory.openSession();
    }
}
