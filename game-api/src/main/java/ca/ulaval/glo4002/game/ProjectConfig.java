package ca.ulaval.glo4002.game;

import ca.ulaval.glo4002.game.interfaces.rest.HeartbeatResource;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ProjectConfig extends ResourceConfig {

    public ProjectConfig() {
        registerResources();
    }

    private void registerResources() {
        // Todo Initialiser le service ici


        // Todo Initialiser la ressource ici
        HeartbeatResource heartbeatResource = new HeartbeatResource();

        register(new Root());
        // Todo Enregistrer la ressource ici
        register(heartbeatResource);
    }

    @Path("/")
    public static class Root {
        @GET
        public Response ping() {
            return Response.ok("Game Api is alive", MediaType.TEXT_PLAIN).build();
        }
    }
}
