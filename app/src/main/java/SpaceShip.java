// SpaceShip.java
// Used for SpaceInvader
/**
 * Modified to fulfill the proposed requirements for the Space Invaders Plus.
 * by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */


import ch.aplu.jgamegrid.*;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.List;

public class SpaceShip extends Actor implements GGKeyListener
{
  private int nbShots = 0;
  private SpaceInvader spaceInvader;
  private boolean isAutoTesting = false;
  private List<String> controls = null;
  private int controlIndex = 0;
  public SpaceShip(SpaceInvader spaceInvader)
  {
    super("sprites/spaceship.gif");
    this.spaceInvader = spaceInvader;
  }

  public void setTestingConditions(boolean isAutoTesting, List<String> controls) {
    this.isAutoTesting = isAutoTesting;
    this.controls = controls;
  }

  private void autoMove() {
    if (isAutoTesting) {
      if (controls != null && controlIndex < controls.size()) {
        String control = controls.get(controlIndex);
        Location next = null;

        switch(control) {
          case "L":
            next = getLocation().getAdjacentLocation(Location.WEST);
            moveTo(next);
            break;

          case "R":
            next = getLocation().getAdjacentLocation(Location.EAST);
            moveTo(next);
            break;

          case "F":
            Bomb bomb = new Bomb();
            gameGrid.addActor(bomb, getLocation());
            nbShots++;
            break;
          case "E":
            spaceInvader.setIsGameOver(true);
            break;
        }
        controlIndex++;
      }
    }
  }

  public void act()
  {
    autoMove();
    spaceInvader.getBg().clear();
    spaceInvader.getBg().drawText("Number of shots: " + nbShots, new Point(10, 30));

    // Movement Speed changes only occur in the extended version of the game
    if (spaceInvader.getConfiguration().equals("plus")) {
      spaceInvader.notifyAliensMoveFast(nbShots, spaceInvader.getActors(Alien.class));
    }

    Location location = getLocation();
    if (spaceInvader.getNumberOfActorsAt(location, Alien.class) > 0)
    {
      spaceInvader.removeAllActors();
      spaceInvader.addActor(new Actor("sprites/explosion2.gif"), location);
      spaceInvader.setIsGameOver(true);
      return;
    }
    if (spaceInvader.getNumberOfActors(Alien.class) == 0)
    {
      spaceInvader.getBg().drawText("Game constructed with JGameGrid (www.aplu.ch)", new Point(10, 50));
      spaceInvader.addActor(new Actor("sprites/you_win.gif"), new Location(100, 60));
      spaceInvader.setIsGameOver(true);
      return;
    }
  }

  public boolean keyPressed(KeyEvent keyEvent)
  {
    Location next = null;
    switch (keyEvent.getKeyCode())
    {
      case KeyEvent.VK_LEFT:
        next = getLocation().getAdjacentLocation(Location.WEST);
        moveTo(next);
        break;

      case KeyEvent.VK_RIGHT:
        next = getLocation().getAdjacentLocation(Location.EAST);
        moveTo(next);
        break;

      case KeyEvent.VK_SPACE:
        Bomb bomb = new Bomb();
        gameGrid.addActor(bomb, getLocation());
        nbShots++;
        break;
    }

    return false;
  }

  private void moveTo(Location location)
  {
    if (location.x > 10 && location.x < 190)
      setLocation(location);
  }

  @Override
  public boolean keyReleased(KeyEvent keyEvent) {
    return false;
  }
}
