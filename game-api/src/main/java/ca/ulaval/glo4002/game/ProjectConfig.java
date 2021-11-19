package ca.ulaval.glo4002.game;

import ca.ulaval.glo4002.game.applicationService.*;
import ca.ulaval.glo4002.game.applicationService.dinosaur.DinosaurAssembler;
import ca.ulaval.glo4002.game.applicationService.dinosaur.DinosaurService;
import ca.ulaval.glo4002.game.applicationService.food.FoodAssembler;
import ca.ulaval.glo4002.game.applicationService.food.FoodSummaryAssembler;
import ca.ulaval.glo4002.game.applicationService.food.ResourceService;
import ca.ulaval.glo4002.game.domain.Game;
import ca.ulaval.glo4002.game.domain.GameRepository;
import ca.ulaval.glo4002.game.domain.Turn;
import ca.ulaval.glo4002.game.domain.dinosaur.BabyFetcher;
import ca.ulaval.glo4002.game.domain.dinosaur.DinosaurFactory;
import ca.ulaval.glo4002.game.domain.dinosaur.herd.CarnivorousDinosaurFeeder;
import ca.ulaval.glo4002.game.domain.dinosaur.herd.HerbivorousDinosaurFeeder;
import ca.ulaval.glo4002.game.domain.dinosaur.herd.Herd;
import ca.ulaval.glo4002.game.domain.food.*;
import ca.ulaval.glo4002.game.infrastructure.GameRepositoryInMemory;
import ca.ulaval.glo4002.game.infrastructure.dinosaur.dinosaurBreederExternal.*;
import ca.ulaval.glo4002.game.interfaces.rest.dinosaur.DinosaurResource;
import ca.ulaval.glo4002.game.interfaces.rest.food.FoodResource;
import ca.ulaval.glo4002.game.interfaces.rest.food.FoodValidator;
import ca.ulaval.glo4002.game.interfaces.rest.game.GameResource;
import ca.ulaval.glo4002.game.interfaces.rest.mappers.*;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.ArrayList;
import java.util.List;

public class ProjectConfig extends ResourceConfig {

    public ProjectConfig() {
        registerResources();
        registerExceptionMappers();
    }

    private void registerResources() {
        GameRepository gameRepository = new GameRepositoryInMemory();

        FoodProvider foodProvider = new CookItSubscription();

        Game game = gameRepository
                    .find()
                    .orElse(new Game(
                            new Herd(new ArrayList<>(), List.of(new CarnivorousDinosaurFeeder(),
                                    new HerbivorousDinosaurFeeder())),
                            new Pantry(foodProvider),
                            new Turn()
                    ));

        Pantry pantry = game.getPantry();
        Herd herd = game.getHerd();

        FoodQuantitySummaryCalculator foodQuantitySummaryCalculator = new FoodQuantitySummaryCalculator();
        FoodValidator foodValidator = new FoodValidator();

        DinosaurFactory dinosaurFactory = new DinosaurFactory(pantry, pantry);

        DinosaurBreederExternal dinoBreeder = new DinosaurBreederExternal();
        ParentsGenderValidator parentsGenderValidator = new ParentsGenderValidator();
        BabyFetcher dinosaurBabyFetcher
                = new BabyFetcherFromExternalAPI(dinoBreeder, dinosaurFactory, parentsGenderValidator);

        TurnAssembler turnAssembler = new TurnAssembler();
        FoodAssembler foodAssembler = new FoodAssembler();
        DinosaurAssembler dinosaurAssembler = new DinosaurAssembler();
        FoodSummaryAssembler foodSummaryAssembler = new FoodSummaryAssembler(foodAssembler);

        ResourceService resourceService = new ResourceService(foodQuantitySummaryCalculator, pantry, game);
        DinosaurService dinosaurService = new DinosaurService(dinosaurFactory, herd, game, dinosaurBabyFetcher);
        GameService gameService = new GameService(game, gameRepository);

        GameResource gameResource = new GameResource(gameService, turnAssembler);
        FoodResource foodResource = new FoodResource(resourceService, foodValidator, foodAssembler,
                foodSummaryAssembler);
        DinosaurResource dinosaurResource = new DinosaurResource(dinosaurService, dinosaurAssembler);

        register(gameResource);
        register(foodResource);
        register(dinosaurResource);
    }

    private void registerExceptionMappers() {
        register(new DuplicateNameExceptionMapper());
        register(new InvalidGenderExceptionMapper());
        register(new InvalidSpeciesExceptionMapper());
        register(new InvalidWeightExceptionMapper());
        register(new NonExistentNameExceptionMapper());
        register(new InvalidResourceQuantityExceptionMapper());
        register(new InvalidFatherExceptionMapper());
        register(new InvalidMotherExceptionMapper());
    }
}
