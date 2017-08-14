package io.houthacker.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.houthacker.notifications.conf.MySQLConfiguration;

/**
 * @author houthacker
 */
public class NotificationsConfiguration extends Configuration {

    @JsonProperty("mysql-configuration")
    private MySQLConfiguration mySQLConfiguration = new MySQLConfiguration();
}
