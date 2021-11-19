package ca.ulaval.glo4002.game.domain.dinosaur.herd;

import ca.ulaval.glo4002.game.domain.dinosaur.Dinosaur;
import ca.ulaval.glo4002.game.domain.dinosaur.Gender;
import ca.ulaval.glo4002.game.domain.dinosaur.Species;
import ca.ulaval.glo4002.game.domain.dinosaur.consumption.FoodConsumptionStrategy;
import ca.ulaval.glo4002.game.domain.dinosaur.consumption.FoodNeed;
import ca.ulaval.glo4002.game.domain.dinosaur.exceptions.NonExistentNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


public class HerdTest {

    private final static String DINOSAUR_NAME = "Bobi";
    private final static String ANOTHER_DINOSAUR_NAME = "Alyce";
    private final static String NON_EXISTING_DINOSAUR_NAME = "Alfred";
    private final static int DINOSAUR_WEIGHT = 80;
    private final static int ANOTHER_DINOSAUR_WEIGHT= 100;

    private FoodConsumptionStrategy dinosaurStrategy;
    private FoodConsumptionStrategy anotherDinosaurStrategy;
    private Dinosaur aDinosaurInHerd;
    private Dinosaur anotherDinosaurInHerd;
    private final List<Dinosaur> dinosaurs = new ArrayList<>();
    private DinosaurFeeder aDinosaurFeeder;
    private DinosaurFeeder anotherDinosaurFeeder;
    private final List<DinosaurFeeder> dinosaurFeeders = new ArrayList<>();
    private Herd herd;

    @BeforeEach
    void setUp() {
        dinosaurStrategy = mock(FoodConsumptionStrategy.class);
        anotherDinosaurStrategy = mock(FoodConsumptionStrategy.class);

        aDinosaurInHerd = new Dinosaur(Species.Allosaurus, DINOSAUR_WEIGHT, DINOSAUR_NAME,
                Gender.M, dinosaurStrategy);
        anotherDinosaurInHerd = new Dinosaur(Species.TyrannosaurusRex, ANOTHER_DINOSAUR_WEIGHT,
                ANOTHER_DINOSAUR_NAME, Gender.M, anotherDinosaurStrategy);
        dinosaurs.add(aDinosaurInHerd);
        dinosaurs.add(anotherDinosaurInHerd);

        aDinosaurFeeder = mock(DinosaurFeeder.class);
        anotherDinosaurFeeder = mock(DinosaurFeeder.class);
        herd = new Herd(dinosaurs,List.of(aDinosaurFeeder,anotherDinosaurFeeder));
    }

    @Test
    public void givenADinosaurWithNameNotAlreadyExisting_whenAddingNotExistingDinosaur_thenDinosaurShouldBeAdded() {
        Dinosaur aDinosaurWithNonExistingName = new Dinosaur(Species.Allosaurus,DINOSAUR_WEIGHT,
                NON_EXISTING_DINOSAUR_NAME, Gender.M,dinosaurStrategy);

        herd.addDinosaur(aDinosaurWithNonExistingName);

        assertTrue(dinosaurs.contains(aDinosaurWithNonExistingName));
    }

    @Test
    public void givenADinosaurWithNameAlreadyExisting_whenAddDinosaur_thenDinosaurShouldNotBeAdded() {
        Dinosaur aDinosaurWithExistingName = new Dinosaur(Species.Allosaurus,DINOSAUR_WEIGHT,
                DINOSAUR_NAME, Gender.M,dinosaurStrategy);

        herd.addDinosaur(aDinosaurWithExistingName);

        assertFalse(dinosaurs.contains(aDinosaurWithExistingName));
    }

    @Test
    public void givenANameOfANonExistingDinosaurInHerd_whenHasDinosaurWithName_thenNameShouldNotExist() {
        boolean dinosaurNameExists = herd.hasDinosaurWithName(NON_EXISTING_DINOSAUR_NAME);

        assertFalse(dinosaurNameExists);
    }

    @Test
    public void givenANameOfAnExistingDinosaurInHerd_whenHasDinosaurWithName_thenNameShouldExist() {
        boolean dinosaurNameExists = herd.hasDinosaurWithName(DINOSAUR_NAME);

        assertTrue(dinosaurNameExists);
    }

    @Test
    public void givenANameOfANonExistingDinosaurInHerd_whenGetDinosaurWithName_thenShouldThrowNonExistentNameException() {
        assertThrows(NonExistentNameException.class, () -> herd.getDinosaurWithName(NON_EXISTING_DINOSAUR_NAME));
    }

    @Test
    public void givenANameOfAnExistingDinosaurInHerd_whenGetDinosaurWithName_thenDinosaurShouldBeFound() {
        Dinosaur dinosaurWithTheName = herd.getDinosaurWithName(DINOSAUR_NAME);

        assertEquals(aDinosaurInHerd,dinosaurWithTheName);
    }

    @Test
    public void givenHerd_whenFeedDinosaurs_thenOnlyFastingDinosaursShouldBeRemoved() {
        when(dinosaurStrategy.areFoodNeedsSatisfied()).thenReturn(true);
        when(dinosaurStrategy.getFoodNeeds(anyInt(),anyInt())).thenReturn(new ArrayList<>());
        when(anotherDinosaurStrategy.areFoodNeedsSatisfied()).thenReturn(false);
        when(anotherDinosaurStrategy.getFoodNeeds(anyInt(),anyInt())).thenReturn(new ArrayList<>());

        herd.feedDinosaurs();

        assertTrue(dinosaurs.contains(aDinosaurInHerd));
        assertFalse(dinosaurs.contains(anotherDinosaurInHerd));
    }

    @Test
    public void givenHerd_whenFeedDinosaurs_thenAllDinosaurFeederShouldFeed() {
        List<FoodNeed> dinosaurFoodNeeds = new ArrayList<>();
        List<FoodNeed> anotherDinosaurFoodNeeds = new ArrayList<>();
        when(dinosaurStrategy.getFoodNeeds(anyInt(),anyInt())).thenReturn(dinosaurFoodNeeds);
        when(anotherDinosaurStrategy.getFoodNeeds(anyInt(),anyInt())).thenReturn(anotherDinosaurFoodNeeds);

        herd.feedDinosaurs();

        ArgumentMatcher<Map<Dinosaur,List<FoodNeed>>> isCorrectDinosaursWithNeed =
                map -> map.get(aDinosaurInHerd).equals(dinosaurFoodNeeds)
                        && map.get(anotherDinosaurInHerd).equals(anotherDinosaurFoodNeeds);
        verify(aDinosaurFeeder).feedDinosaurs(argThat(isCorrectDinosaursWithNeed));
        verify(anotherDinosaurFeeder).feedDinosaurs(argThat(isCorrectDinosaursWithNeed));
    }

    @Test
    public void givenADinosaurInHerd_whenIncreaseDinosaursAge_ThenDinosaurAgeShouldIncrease() {
        Dinosaur fakeDinosaur = mock(Dinosaur.class);
        herd.addDinosaur(fakeDinosaur);

        herd.increaseDinosaursAge();

        verify(fakeDinosaur).age();
    }

    @Test
    public void givenHerd_whenReset_thenHerdShouldBeEmpty() {
        herd.reset();

        assertTrue(dinosaurs.isEmpty());
    }

    @Test
    public void givenHerd_whenGetAllDinosaurs_thenAllDinosaursShouldBeReturned(){
        List<Dinosaur> allDinosaursInHerd = herd.getAllDinosaurs();

        assertEquals(dinosaurs, allDinosaursInHerd);
    }
}
