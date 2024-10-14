/**
 * Implements a powerful alien that takes 5 hits to kill.
 * by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class PowerfulAlien extends Alien {
    private static final String IMAGE_NAME = "sprites/powerful_alien.gif";

    private int health = 5;

    public PowerfulAlien(int rowIndex, int colIndex) {
        super(IMAGE_NAME, rowIndex, colIndex);
        type = "powerful";
    }

    @Override
    public void handleHit() {
        --health;

        if (health == 0)
            removeSelf();
    }
}
