package ca.ulaval.glo4002.game.domain;

import ca.ulaval.glo4002.game.domain.food.CookItSubscription;
import ca.ulaval.glo4002.game.domain.food.Food;
import ca.ulaval.glo4002.game.domain.food.FoodType;
import ca.ulaval.glo4002.game.domain.food.Pantry;
import ca.ulaval.glo4002.game.domain.action.AddFoodAction;
import ca.ulaval.glo4002.game.domain.action.ExecutableAction;

import java.util.*;

public class Game {

    private final Turn turn;
    private final CookItSubscription cookItSubscription;
    private final Pantry pantry;

    public Game(Pantry pantry, Turn turn, CookItSubscription cookItSubscription) {
        this.pantry = pantry;
        this.turn = turn;
        this.cookItSubscription = cookItSubscription;
    }

    public void addFood(Map<FoodType, Food> foods) {
        ExecutableAction addFoodAction = new AddFoodAction(pantry, foods);
        turn.acquireNewAction(addFoodAction);
    }

    public int playTurn() {
        int turnNumber = turn.playActions();
        pantry.incrementFreshFoodAges();
        pantry.addFoodFromCookITToNewFood(cookItSubscription);
        pantry.addNewFoodToFreshFood();
        pantry.removeExpiredFoodFromFreshFood();
        return turnNumber;
    }

    public void reset() {
        turn.reset();
        pantry.reset();
    }
}
