package ca.ulaval.glo4002.game.domain.dinosaur;

import ca.ulaval.glo4002.game.domain.dinosaur.consumption.CarnivorousFoodConsumptionStrategy;
import ca.ulaval.glo4002.game.domain.dinosaur.consumption.CarnivorousFoodStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CarnivorousFoodConsumptionStrategyTest {

    private final int OTHER_AGE = 4;
    private final int WEIGHT = 81;
    private final int EXPECTED_NORMAL_BURGERS = 1;
    private final int EXPECTED_NORMAL_WATER = 49;
    private final int EXPECTED_DOUBLE_BURGERS = 1;
    private final int EXPECTED_DOUBLE_WATER = 98;

    private CarnivorousFoodConsumptionStrategy strategy;
    private CarnivorousFoodStorage foodStorage;

    @BeforeEach
    public void setup() {
        foodStorage = mock(CarnivorousFoodStorage.class);
        strategy = new CarnivorousFoodConsumptionStrategy(foodStorage);
    }

    @Test
    public void givenAgeIsNot0_whenConsumingFood_thenItShouldTakeTheRightAmount() {
        strategy.consumeFood(WEIGHT, OTHER_AGE);

        verify(foodStorage).giveExactOrMostPossibleBurgerDesired(EXPECTED_NORMAL_BURGERS);
        verify(foodStorage).giveExactOrMostPossibleWaterDesired(EXPECTED_NORMAL_WATER);
    }

    @Test
    public void givenAgeIs0_whenConsumingFood_thenItShouldTakeTheDoubleOfRightAmount() {
        strategy.consumeFood(WEIGHT, 0);

        verify(foodStorage).giveExactOrMostPossibleBurgerDesired(EXPECTED_DOUBLE_BURGERS);
        verify(foodStorage).giveExactOrMostPossibleWaterDesired(EXPECTED_DOUBLE_WATER);
    }
}
