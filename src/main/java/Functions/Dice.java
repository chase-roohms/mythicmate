package Functions;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    private final int sides;

    //Init sides to given int
    public Dice(int faces) {
        sides = faces;
    }

    //Return a random number from 1 to # of sides
    //Uses ThreadLocalRandom for thread-safe, high-performance randomness in concurrent bot commands
    public int roll() {
        return ThreadLocalRandom.current().nextInt(sides) + 1;
    }
}