/**
 *  Implements the NormalAlien class, which is a subclass of the Alien class.
 *  by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */
public class NormalAlien extends Alien {
    private static final String IMAGE_NAME = "sprites/alien.gif";

    public NormalAlien(int rowIndex, int colIndex) {
        super(IMAGE_NAME, rowIndex, colIndex);
        type = "alien";
    }

    public NormalAlien(int rowIndex, int colIndex, int nbSteps, int speedFactor)
    {
        super(IMAGE_NAME, rowIndex, colIndex, nbSteps, speedFactor);
        type = "alien";
    }
}
