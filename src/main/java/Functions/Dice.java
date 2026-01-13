package Functions;

import java.util.Random;

public class Dice {
    private final int sides;

    //Init sides to given int
    public Dice(int faces) {
        sides = faces;
    }

    //Return a random number from 1 to # of sides
    public int roll() {
        Random random = new Random();
        return random.nextInt(sides) + 1;
    }
}