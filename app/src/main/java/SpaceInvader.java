// SpaceInvader.java
// Sprite images from http://www.cokeandcode.com/tutorials
// Nice example how the different actor classes: SpaceShip, Bomb, SpaceInvader, Explosion
// act almost independently of each other. This decoupling simplifies the logic of the application

/**
 *
 * Modified to fulfill the proposed requirements for the Space Invaders Plus.
 * by Monday 17:15 Group 22 Evan Liapakis, Claire Tosolini and Toby Guan
 */


import ch.aplu.jgamegrid.*;

import java.awt.Point;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.*;

public class SpaceInvader extends GameGrid implements GGKeyListener
{
  private final String CONFIGURATION;
  private int nbRows = 3;
  private int nbCols = 11;
  private boolean isGameOver = false;
  private boolean isAutoTesting = false;
  private Properties properties = null;
  private StringBuilder logResult = new StringBuilder();
  private ArrayList<AlienGridLocation> powerfulAlienLocations = new ArrayList<AlienGridLocation>();
  private ArrayList<AlienGridLocation> invulnerableAlienLocations = new ArrayList<AlienGridLocation>();
  private ArrayList<AlienGridLocation> multipleAlienLocations = new ArrayList<AlienGridLocation>();
  private Alien[][] alienGrid = null;
  public SpaceInvader(Properties properties) {
    super(200, 100, 5, false);
    this.properties = properties;
    this.CONFIGURATION = properties.getProperty("version");
  }

  private ArrayList<AlienGridLocation> convertFromProperty(String propertyName) {
    String powerfulAlienString = properties.getProperty(propertyName);
    ArrayList<AlienGridLocation> alienLocations = new ArrayList<>();
    if (powerfulAlienString != null) {
      String [] locations = powerfulAlienString.split(";");
      for (String location: locations) {
        String [] locationPair = location.split("-");
        int rowIndex = Integer.parseInt(locationPair[0]);
        int colIndex = Integer.parseInt(locationPair[1]);
        alienLocations.add(new AlienGridLocation(rowIndex, colIndex));
      }
    }

    return alienLocations;
  }

  private void setupAlienLocations() {
    powerfulAlienLocations = convertFromProperty("Powerful.locations");
    invulnerableAlienLocations = convertFromProperty("Invulnerable.locations");
    multipleAlienLocations = convertFromProperty("Multiple.locations");
  }

  private boolean arrayContains(ArrayList<AlienGridLocation>locations, int rowIndex, int colIndex) {
    for (AlienGridLocation location : locations) {
      if (location.rowIndex == rowIndex && location.colIndex == colIndex) {
        return true;
      }
    }

    return false;
  }

  private void setupAliens() {
    setupAlienLocations();
    isAutoTesting = Boolean.parseBoolean(properties.getProperty("isAuto"));
    String aliensControl = properties.getProperty("aliens.control");
    List<String> movements = null;
    if (aliensControl != null) {
      movements = Arrays.asList(aliensControl.split(";"));
    }
    alienGrid = new Alien[nbRows][nbCols];
    for (int i = 0; i < nbRows; i++) {
      for (int k = 0; k < nbCols; k++) {
        Alien alien;
        if (arrayContains(powerfulAlienLocations, i, k)) {
          alien = new PowerfulAlien(i, k);
        } else if (arrayContains(invulnerableAlienLocations, i, k)) {
          alien = new InvulnerableAlien(i, k);
        } else if (arrayContains(multipleAlienLocations, i, k)) {
          alien = new MultipleAlien(i, k);
        } else {
          alien = new NormalAlien(i, k);
        }
        addActor(alien, new Location(100 - 5 * nbCols + 10 * k, 10 + 10 * i));
        alien.setTestingConditions(isAutoTesting, movements);
        alienGrid[i][k] = alien;
      }
    }
  }

  private void setupSpaceShip() {
    SpaceShip ss = new SpaceShip(this);
    addActor(ss, new Location(100, 90));

    String spaceShipControl = properties.getProperty("space_craft.control");
    List<String> controls = null;
    if (spaceShipControl != null) {
      controls = Arrays.asList(spaceShipControl.split(";"));
    }

    ss.setTestingConditions(isAutoTesting, controls);
    addKeyListener(ss);
  }


  public String runApp(boolean isDisplayingUI) {
    setSimulationPeriod(Integer.parseInt(properties.getProperty("simulationPeriod")));
    nbRows = Integer.parseInt(properties.getProperty("rows"));
    nbCols = Integer.parseInt(properties.getProperty("cols"));
    setupAliens();
    setupSpaceShip();
    addKeyListener(this);
    getBg().setFont(new Font("SansSerif", Font.PLAIN, 12));
    getBg().drawText("Use <- -> to move, spacebar to shoot", new Point(400, 330));
    getBg().drawText("Press any key to start...", new Point(400, 350));

    if (isDisplayingUI) {
      show();
    }

    if (isAutoTesting) {
      setBgColor(java.awt.Color.black);  // Erase text
      doRun();
    }

    while(!isGameOver) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    doPause();
    return logResult.toString();
  }

  @Override
  public void act() {
    logResult.append("Alien locations: ");
    for (int i = 0; i < nbRows; i++) {
      for (int j = 0; j < nbCols; j++) {
        Alien alienData = alienGrid[i][j];

        String isDeadStatus = alienData.isRemoved() ? "Dead" : "Alive";
        String gridLocation = "0-0";
        if (!alienData.isRemoved()) {
          gridLocation = alienData.getX() + "-" + alienData.getY();
        }
        String alienDataString = String.format("%s@%d-%d@%s@%s#", alienData.getType(),
                alienData.getRowIndex(), alienData.getColIndex(), isDeadStatus, gridLocation);
        logResult.append(alienDataString);
      }
    }
    logResult.append("\n");
  }

  public void notifyAliensMoveFast(int nbShots, List<Actor> actors) {
    boolean speedChanged = false;

    for (Actor actor : actors) {
      Alien alien = (Alien) actor;
      int oldSpeedFactor = alien.getSpeedFactor();
      int speedFactor = oldSpeedFactor;
      if (nbShots == 10) {
        speedFactor = 2;
      } else if (nbShots == 50) {
        speedFactor = 3;
      } else if (nbShots == 100) {
        speedFactor = 4;
      } else if (nbShots == 500) {
        speedFactor = 5;
      }

      alien.setSpeedFactor(speedFactor);

      if (speedFactor != oldSpeedFactor) {
        speedChanged = true;
      }
    }

    if (speedChanged) {
      logResult.append("Aliens start moving fast");
    }
  }

  public void notifyAlienHit(List<Actor> actors) {
    for (Actor actor: actors) {
      Alien alien = (Alien)actor;
      alien.handleHit();
      String alienData = String.format("%s@%d-%d",
              alien.getType(), alien.getRowIndex(), alien.getColIndex());
      logResult.append("An alien has been hit.");
      logResult.append(alienData + "\n");
    }
  }

  public void setIsGameOver(boolean isOver) {
    isGameOver = isOver;
  }

  public boolean keyPressed(KeyEvent evt)
  {
    if (!isRunning())
    {
      setBgColor(java.awt.Color.black);  // Erase text
      doRun();
    }
    // exit game if user presses the Esc key
    if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
      exitGame();
      return true;
    }
    return false;  // Do not consume key
  }

  private void exitGame() {
    // pause the game if it's currently running
    doPause();
    // terminate the program
    System.exit(0);
  }

  public boolean keyReleased(KeyEvent evt)
  {
    return false;
  }

  public boolean increaseAliens(MultipleAlien multipleAlien) {
    if (!checkTopRow())
      return false;

    int multipleAlienX = multipleAlien.getLocation().getX();
    int babyAlienStartingX = multipleAlienX - 10 * multipleAlien.getColIndex();
    double direction = multipleAlien.getDirection();

    System.out.println(multipleAlien.getLocation());
    System.out.println(multipleAlien.getLocation().getNeighbourLocation(direction));

    // add aliens
    for (int i = 0; i < nbCols; ++i) {
      Alien alien = new NormalAlien(0, i, multipleAlien.getNbSteps(), multipleAlien.getSpeedFactor());
      addActor(alien, new Location(babyAlienStartingX + 10 * i, 10), direction);
    }

    Alien newAlien = new NormalAlien(multipleAlien.getRowIndex(), multipleAlien.getColIndex(), multipleAlien.getNbSteps(), multipleAlien.getSpeedFactor());
    addActor(newAlien, multipleAlien.getLocation(), direction);


    // reset all actors to resynchronise
    // looks whack for one action and then fixes itself
    List<Actor> actors = getActors(Alien.class);

    for (Actor actor : actors) {
      Alien alien = (Alien) actor;
      alien.reset();
    }

    return true;
  }

  private boolean checkTopRow() {
    for (int i = 0; i < this.getNbHorzCells(); ++i) {
      List<Actor> actors = getActorsAt(new Location(i, 10), Alien.class);
      // this ensures proper spacing
      actors.addAll(getActorsAt(new Location(i, 15), Alien.class));
      if (actors.size() > 0) {
        return false;
      }
    }

    return true;
  }

  public String getConfiguration() {
    return this.CONFIGURATION;
  }
}
