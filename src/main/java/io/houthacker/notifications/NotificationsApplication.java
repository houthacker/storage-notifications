package io.houthacker.notifications;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.houthacker.notifications.backend.Backend;
import io.houthacker.notifications.backend.mysql.MySQLBackend;
import io.houthacker.notifications.notifier.Notifier;
import io.houthacker.notifications.resources.NotificationsResource;

/**
 * @author houthacker
 */
public class NotificationsApplication extends Application<NotificationsConfiguration> {

    private static final String hibernatePackage = "io.houthacker.notifications.backends";

    private final HibernateBundle<NotificationsConfiguration> hibernate = new ScanningHibernateBundle<NotificationsConfiguration>(hibernatePackage) {
        public PooledDataSourceFactory getDataSourceFactory(NotificationsConfiguration notificationsConfiguration) {
            return notificationsConfiguration.getMySqlConfiguration().getDataSourceFactory();
        }
    };

    public static void main(String... args) throws Exception {
        new NotificationsApplication().run(args);
    }

    @Override
    public String getName() {
        return "storage-notifications";
    }

    @Override
    public void initialize(Bootstrap<NotificationsConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(NotificationsConfiguration conf, Environment environment) throws Exception {
        final Notifier notifier = new Notifier();
        final Backend mysql = new MySQLBackend(hibernate.getSessionFactory(), notifier
                , conf.getMySqlConfiguration().getListenHost(), conf.getMySqlConfiguration().getListenPort());

        final NotificationsResource notificationsResource = new NotificationsResource(mysql);
        environment.jersey().register(notificationsResource);
    }
}
