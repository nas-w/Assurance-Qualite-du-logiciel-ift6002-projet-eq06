package ca.ulaval.glo4002.game.domain.dinosaur;

import ca.ulaval.glo4002.game.domain.dinosaur.consumption.FoodConsumptionStrategy;

public class BabyDinosaur extends Dinosaur {

    protected final static int INITIAL_BABY_WEIGHT = 1;

    protected Dinosaur fatherDinosaur;
    protected Dinosaur motherDinosaur;

    public BabyDinosaur(Species species, String name, Gender gender,
                        FoodConsumptionStrategy foodConsumptionStrategy, Dinosaur fatherDinosaur,
                        Dinosaur motherDinosaur) {
        super(species, INITIAL_BABY_WEIGHT, name, gender, foodConsumptionStrategy);
        this.fatherDinosaur = fatherDinosaur;
        this.motherDinosaur = motherDinosaur;
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() && (fatherDinosaur.isAlive() || motherDinosaur.isAlive());
    }
}
