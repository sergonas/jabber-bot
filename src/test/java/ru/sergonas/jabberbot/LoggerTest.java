package ru.sergonas.jabberbot;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;
import ru.sergonas.jabberbot.orm.LogEntry;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

/**
 * User: Сергей
 * Date: 14.08.13
 * Time: 11:26
 */
public class LoggerTest {
    Logger logger;
    @Before
    public void setup() throws Exception {
        logger = new Logger();
        assertNotNull(logger);
    }

    @Test
    public void testSave() throws Exception {
        logger.log(new LogEntry(new Date(),"me", "fuck it, im young!"));
    }

    @Test
    public void testCreatedData() throws Exception {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SessionFactory sessionFactory =  new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from LogEntry le where le.name = :who" ).setString("who", "me").list();
        for ( LogEntry entry : (List<LogEntry>) result ) {
            System.out.printf("%s <%s>: %s%n", formatter.format(entry.getTime()), entry.getName(), entry.getMessage());
        }
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
}
