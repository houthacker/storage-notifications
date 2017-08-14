package io.houthacker.notifications.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author houthacker
 */
public class MySQLConfiguration {

    @NotEmpty
    private String listenHost;

    @NotEmpty
    private int listenPort;

    @JsonProperty
    public String getListenHost() {
        return listenHost;
    }

    @JsonProperty
    public void setListenHost(String listenHost) {
        this.listenHost = listenHost;
    }

    @JsonProperty
    public int getListenPort() {
        return listenPort;
    }

    @JsonProperty
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }
}
