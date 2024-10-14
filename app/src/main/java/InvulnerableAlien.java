/**
 * Implements an invulnerable alien that toggles between invulnerable and vulnerable states.
 * by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */

public class InvulnerableAlien extends Alien {
    private static final String IMAGE_NAME = "sprites/invulnerable_alien.gif";
    private static final int INVULNERABLE_TIMER = 4;
    private static final int VULNERABLE_TIMER = 8;

    private boolean isInvulnerable = false;
    private int timer = VULNERABLE_TIMER;

    public InvulnerableAlien(int rowIndex, int colIndex) {
        super(IMAGE_NAME, rowIndex, colIndex);
        type = "invulnerable";
    }

    @Override
    public void handleHit() {
        if (!isInvulnerable)
            removeSelf();
    }

    @Override
    public void act() {
        super.act();

        --timer;

        // use a timer to toggle invulnerability
        if (timer == 0) {
            if (isInvulnerable) {
                isInvulnerable = false;
                timer = VULNERABLE_TIMER;
            } else {
                isInvulnerable = true;
                timer = INVULNERABLE_TIMER;
            }
        }
    }
}
