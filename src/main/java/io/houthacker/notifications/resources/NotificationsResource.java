package io.houthacker.notifications.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.houthacker.notifications.backend.Backend;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static java.util.Objects.requireNonNull;

/**
 * @author houthacker
 */
@Path("/notifications")
public class NotificationsResource {

    private final Backend backend;

    public NotificationsResource(final Backend backend) {
        requireNonNull(backend);
        this.backend = backend;
    }

    @GET
    @Path("/test")
    @UnitOfWork
    public Response test() {
        if (!this.backend.isBootstrapped()) {
            this.backend.bootstrap();
        }
        return Response.ok().build();
    }
}
