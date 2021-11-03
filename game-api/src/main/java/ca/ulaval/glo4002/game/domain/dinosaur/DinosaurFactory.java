package ca.ulaval.glo4002.game.domain.dinosaur;

import ca.ulaval.glo4002.game.domain.dinosaur.consumption.*;
import ca.ulaval.glo4002.game.domain.dinosaur.exceptions.InvalidGenderException;
import ca.ulaval.glo4002.game.domain.dinosaur.exceptions.InvalidSpeciesException;
import ca.ulaval.glo4002.game.domain.food.FoodStorage;

public class DinosaurFactory {

    private final CarnivorousFoodStorage carnivorousFoodStorage;
    private final HerbivorousFoodStorage herbivorousFoodStorage;
    private final FoodStorage foodStorage;

    public DinosaurFactory(CarnivorousFoodStorage carnivorousFoodStorage,
                           HerbivorousFoodStorage herbivorousFoodStorage, FoodStorage foodStorage) {
        this.carnivorousFoodStorage = carnivorousFoodStorage;
        this.herbivorousFoodStorage = herbivorousFoodStorage;
        this.foodStorage = foodStorage;
    }

    public Dinosaur create(String genderName, int weight, String speciesName, String name) {
        Gender gender = findCorrespondingGender(genderName);
        Species species = findCorrespondingSpecies(speciesName);
        FoodConsumptionStrategy foodConsumptionStrategy = findCorrespondingFoodConsumptionStrategy(species);

        return new Dinosaur(species, weight, name, gender, foodConsumptionStrategy);
    }

    public BabyDinosaur createBaby(String genderName, String speciesName, String name, Dinosaur fatherDinosaur,
                                   Dinosaur motherDinosaur) {
        Gender gender = findCorrespondingGender(genderName);
        Species species = findCorrespondingSpecies(speciesName);
        FoodConsumptionStrategy foodConsumptionStrategy = findCorrespondingFoodConsumptionStrategy(species);

        return new BabyDinosaur(species, name, gender, foodConsumptionStrategy, fatherDinosaur, motherDinosaur);
    }

    private Gender findCorrespondingGender(String gender) {
        gender = gender.toUpperCase();
        try {
            return Gender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            throw new InvalidGenderException();
        }

    }

    private Species findCorrespondingSpecies(String species) {
        species = species.replace(" ", "");
        try {
            return Species.valueOf(species);
        } catch(IllegalArgumentException e) {
            throw new InvalidSpeciesException();
        }
    }

    private FoodConsumptionStrategy findCorrespondingFoodConsumptionStrategy(Species species) {
        FoodConsumptionStrategy foodConsumptionStrategy;
        switch(species.getConsumptionType()) {
            case CARNIVOROUS:
                foodConsumptionStrategy = new CarnivorousFoodConsumptionStrategy(carnivorousFoodStorage);
                break;
            case HERBIVOROUS:
                foodConsumptionStrategy = new HerbivorousFoodConsumptionStrategy(herbivorousFoodStorage);
                break;
            case OMNIVOROUS:
                foodConsumptionStrategy = new OmnivorousFoodConsumptionStrategy(foodStorage);
                break;
            default:
                throw new InvalidSpeciesException();
        }
        return foodConsumptionStrategy;
    }
}
