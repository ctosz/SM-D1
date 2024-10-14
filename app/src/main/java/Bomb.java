// Bomb.java
// Used for SpaceInvader
/**
 * Modified to fulfill the proposed requirements for the Space Invaders Plus.
 * by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */


import ch.aplu.jgamegrid.*;

import java.util.List;

public class Bomb extends Actor
{
  public Bomb()
  {
    super("sprites/bomb.gif");
  }

  public void reset()
  {
    setDirection(Location.NORTH);
  }

  public void act()
  {
    // Acts independently searching a possible target and bring it to explosion
    move();
    SpaceInvader spaceInvader = (SpaceInvader) gameGrid;
    List<Actor> actors = gameGrid.getActorsAt(getLocation(), Alien.class);
    if (actors.size() > 0)
    {
      spaceInvader.notifyAlienHit(actors);
      Explosion explosion = new Explosion();
      gameGrid.addActor(explosion, getLocation());
      removeSelf();
      return;
    }
    if (getLocation().y < 5)
      removeSelf();
  }
}
