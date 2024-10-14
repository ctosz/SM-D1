/**
 *  Implementation of a multiple alien that randomly spawn more aliens under certain conditions.
 *  by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

import java.util.Random;
public class MultipleAlien extends Alien {
    private static final String IMAGE_NAME = "sprites/multiple_alien.gif";
    private static final double MULTIPLY_PROBABILITY = 0.1;
    private static final Random RAND = new Random();

    public MultipleAlien(int rowIndex, int colIndex) {
        super(IMAGE_NAME, rowIndex, colIndex);
        type = "multiple";
    }

    @Override
    public void act() {
        super.act();

        // randomly decide whether to multiply
        if (RAND.nextDouble() < MULTIPLY_PROBABILITY) {
            SpaceInvader spaceInvader = (SpaceInvader) gameGrid;

            // initiate multiplication
            if (spaceInvader.increaseAliens(this))
                removeSelf();
        }
    }
}
