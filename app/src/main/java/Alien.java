// Alien.java
// Used for SpaceInvader
/**
 * Modified to fulfill the proposed requirements for the Space Invaders Plus.
 * by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */



import ch.aplu.jgamegrid.*;

import java.util.List;

public abstract class Alien extends Actor
{
  private static final int DEFAULT_SLOWDOWN = 7;
  private final int maxNbSteps = 16;
  private int nbSteps;
  private int speedFactor = 1;
  private boolean isMoving = true;
  private boolean isAutoTesting;
  private List<String> movements;
  private int movementIndex = 0;
  private int rowIndex;
  private int colIndex;
  private boolean isFirstAct = true;
  protected String type;

  public Alien(String imageName, int rowIndex, int colIndex)
  {
    super(imageName);
    this.rowIndex = rowIndex;
    this.colIndex = colIndex;
    nbSteps = 7;
  }

  public Alien(String imageName, int rowIndex, int colIndex, int nbSteps, int speedFactor)
  {
    super(imageName);
    this.rowIndex = rowIndex;
    this.colIndex = colIndex;
    this.nbSteps = nbSteps;
    this.speedFactor = speedFactor;
  }

  @Override
  public void reset() {
    setSlowDown(DEFAULT_SLOWDOWN);
  }

  public String getType() {
    return type;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public int getColIndex() {
    return colIndex;
  }

  public int getSpeedFactor() {
    return speedFactor;
  }

  public int getNbSteps() {
    return nbSteps;
  }

  public int getMaxNbSteps() {
    return maxNbSteps;
  }

  public void setTestingConditions(boolean isAutoTesting, List<String> movements) {
    this.isAutoTesting = isAutoTesting;
    this.movements = movements;
  }

  private void checkMovements() {
    if (isAutoTesting) {
      if (movements != null && movementIndex < movements.size()) {
        String movement = movements.get(movementIndex);
        if (movement.equals("S")) {
          isMoving = false;
        } else if (movement.equals("M")) {
          isMoving = true;
        }
        movementIndex++;
      }
    }
  }

  public void handleHit() {
    removeSelf();
  }

  public void setSpeedFactor(int speedFactor) {
    this.speedFactor = speedFactor;
  }

  public void act() {
    checkMovements();

    // gross but necessary hack to make multiple alien children sync
    if (isFirstAct) {
      isFirstAct = false;
      return;
    }

    if (!isMoving) {
      return;
    }

    // when speeded too fast
    if (nbSteps + speedFactor > maxNbSteps) {
      // calculate the steps about to be skipped
      int stepsLeft = maxNbSteps - nbSteps;

      // move those steps to reach maxNbSteps
      for (int i = 0; i < stepsLeft; i++) {
        move();
      }

      // after that, make a turn
      nbSteps = 0;
      int angle;
      if (getDirection() == 0) {
        angle = 90;
      } else {
        angle = -90;
      }
      turn(angle);
      move();
      turn(angle);
    }

    // normal increments
    else {
      for (int i = 0; i < speedFactor; i++) {
        move();
        nbSteps++;
      }
    }

    if (getLocation().y > 90) {
      removeSelf();
    }
  }

}

 


