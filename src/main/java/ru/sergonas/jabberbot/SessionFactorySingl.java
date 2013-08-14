package ru.sergonas.jabberbot;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * User: Сергей
 * Date: 14.08.13
 * Time: 14:18
 */
public class SessionFactorySingl {
    private static volatile SessionFactory instance;

    public static SessionFactory getInstance() {
        SessionFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (SessionFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    Configuration configuration = new Configuration();
                    configuration.configure();
                    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
                    instance = localInstance = configuration.buildSessionFactory(serviceRegistry);
                }
            }
        }
        return localInstance;
    }
}
