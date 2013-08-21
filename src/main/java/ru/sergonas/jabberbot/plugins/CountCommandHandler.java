package ru.sergonas.jabberbot.plugins;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.sergonas.jabberbot.orm.SessionFactorySingl;
import ru.sergonas.jabberbot.orm.LogEntry;

import java.util.regex.Pattern;

/**
 * User: Сергей
 * Date: 13.08.13
 * Time: 14:54
 */
public class CountCommandHandler implements CommandHandler {
    private static final String COMMAND_NAME = "count";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String executeCommand(String args) {
        String striped = stripe(args);
        long count = findInDB(striped);
        return String.format("Count of \"%s\" is %d", striped, count);
    }

    private long findInDB(String toFind) {
        Session session = SessionFactorySingl.getInstance().openSession();
        Number count = (Number)session.createCriteria(LogEntry.class).add(Restrictions.like("message", "%"+toFind+"%")).setProjection(Projections.rowCount()).uniqueResult();
        return count.longValue();
    }

    private String stripe(String s) {
        if(Pattern.matches("^\"[^\"]+\"$", s)) {
            return s.substring(1, s.length() - 1);
        } else {
            return s;
        }
    }
}
