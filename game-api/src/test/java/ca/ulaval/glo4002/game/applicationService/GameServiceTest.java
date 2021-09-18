package ca.ulaval.glo4002.game.applicationService;

import ca.ulaval.glo4002.game.domain.Game;
import ca.ulaval.glo4002.game.domain.parkResources.TurnResources;
import ca.ulaval.glo4002.game.interfaces.rest.game.TurnNumberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private TurnAssembler turnAssembler;
    private ResourceAssembler resourceAssembler;
    private Game game;
    GameService gameService;

    @BeforeEach
    void setUp() {
        turnAssembler = new TurnAssembler();
        resourceAssembler = new ResourceAssembler();
        game = mock(Game.class);
        gameService = new GameService(turnAssembler, game);
    }

    @Test
    public void whenPlayTurn_thenGameIsPlayed() {
        gameService.playTurn();

        verify(game).playTurn();
    }

    @Test
    public void givenATurnNumber_whenPlayTurn_thenTheAppropriateTurnNumberDTOIsReturned() {
        int aTurnNumber = 143;
        willReturn(aTurnNumber).given(game).playTurn();

        gameService.playTurn();

        TurnNumberDTO turnNumberDTO = turnAssembler.assembleTurnNumber(aTurnNumber);
        assertEquals(aTurnNumber, turnNumberDTO.turnNumber);
    }

    @Test
    public void givenResources_whenPlayTurn_thenResourcesAreAssembled() {
        ArrayList<TurnResources> turnResources = new ArrayList<>();

        
        verify(resourceAssembler).assembleResourceDTO(turnResources);
    }

    @Test
    public void whenReset_thenTurnIsReset() {
        game.reset();

        verify(game).reset();
    }
}
