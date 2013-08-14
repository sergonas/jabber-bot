package ru.sergonas.jabberbot;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sergonas.jabberbot.orm.LogEntry;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 23:38
 */
public class Logger{
    private SessionFactory sessionFactory;
    public Logger() {
        sessionFactory = SessionFactorySingl.getInstance();
    }

    public void log(LogEntry logEntry) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(logEntry);
        session.getTransaction().commit();
        session.close();
    }
}
