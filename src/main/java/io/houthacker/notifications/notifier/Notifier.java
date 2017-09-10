package io.houthacker.notifications.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.houthacker.notifications.backend.notifications.Notification;

import java.net.URI;

/**
 * @author houthacker
 */
public class Notifier {

    private final ObjectMapper mapper;

    public Notifier() {
        this.mapper = new ObjectMapper();
    }

    public void subscribe(final URI endpoint) {

    }

    public void unsubscribe(final URI endpoint) {

    }

    public void notifySubscribersOf(final Notification notification) throws JsonProcessingException {
        System.out.println(mapper.writeValueAsString(notification));
    }

}
