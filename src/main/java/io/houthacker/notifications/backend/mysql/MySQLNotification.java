package io.houthacker.notifications.backend.mysql;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.houthacker.notifications.backend.notifications.Notification;
import io.houthacker.notifications.backend.notifications.NotificationSource;

/**
 * @author houthacker
 */
@JsonPropertyOrder({"source", "schema", "table", "type", "ts"})
public class MySQLNotification implements Notification {

    @JsonProperty
    private String schema;

    @JsonProperty
    private String table;

    @JsonProperty
    private String type;

    @JsonProperty("ts")
    private long timestamp;

    @Override
    @JsonProperty("source")
    public NotificationSource getSource() {
        return NotificationSource.mysql;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
