package io.houthacker.notifications.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author houthacker
 */
public class MySQLConfiguration {

    @NotEmpty
    @JsonProperty
    private String listenHost;

    @NotEmpty
    @JsonProperty
    private int listenPort;

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    public String getListenHost() {
        return listenHost;
    }

    public void setListenHost(String listenHost) {
        this.listenHost = listenHost;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }


    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}
