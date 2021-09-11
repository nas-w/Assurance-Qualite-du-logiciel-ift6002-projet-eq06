package ca.ulaval.glo4002.game;

import ca.ulaval.glo4002.game.applicationService.GameService;
import ca.ulaval.glo4002.game.applicationService.TurnAssembler;
import ca.ulaval.glo4002.game.domain.Game;
import ca.ulaval.glo4002.game.domain.TurnFactory;
import ca.ulaval.glo4002.game.interfaces.rest.game.GameResource;
import ca.ulaval.glo4002.game.interfaces.rest.heartbeat.HeartbeatResource;
import org.glassfish.jersey.server.ResourceConfig;

public class ProjectConfig extends ResourceConfig {

    public ProjectConfig() {
        registerResources();
    }

    private void registerResources() {
        TurnFactory turnFactory = new TurnFactory();
        Game game = new Game(turnFactory);

        TurnAssembler turnAssembler = new TurnAssembler();

        GameService gameService = new GameService(turnAssembler, game);

        HeartbeatResource heartbeatResource = new HeartbeatResource();
        GameResource gameResource = new GameResource(gameService);

        register(heartbeatResource);
        register(gameResource);
    }
}
