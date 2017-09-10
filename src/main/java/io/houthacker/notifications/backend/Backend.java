package io.houthacker.notifications.backend;

/**
 * @author houthacker
 */
public interface Backend {

    /**
     * Starts listening for messages at this backend.
     */
    void bootstrap();

    /**
     *
     * @return Whether the backend has been bootstrapped.
     */
    boolean isBootstrapped();

    /**
     * Notifies the backend that it must stop.
     */
    void stop();
}
