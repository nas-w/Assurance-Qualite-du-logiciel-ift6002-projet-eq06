package ca.ulaval.glo4002.game.applicationService.food;

import ca.ulaval.glo4002.game.domain.food.Food;
import ca.ulaval.glo4002.game.domain.food.FoodType;
import ca.ulaval.glo4002.game.interfaces.rest.food.FoodDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAssembler {

    public List<Food> fromDTO(FoodDTO foodDTO) {
        List<Food> food = new ArrayList<>();

        Food burgers = new Food(FoodType.BURGER, foodDTO.qtyBurger);
        Food salads = new Food(FoodType.SALAD, foodDTO.qtySalad);
        Food water = new Food(FoodType.WATER, foodDTO.qtyWater);

        food.add(burgers);
        food.add(salads);
        food.add(water);

        return food;
    }

    public FoodDTO toDTO(Map<FoodType, Integer> food) {
        FoodDTO foodDTO = new FoodDTO(food.get(FoodType.BURGER), food.get(FoodType.SALAD), food.get(FoodType.WATER));
        return foodDTO;
    }
}
