package ghost;

import java.util.*;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * GameManager class is the overarching system responsible for coordinating between all GameObjects
 * and tracking game progress. GameManager also sets up map layout and object conditions.
 */
public class GameManager {
    
    private Settings settings;
    private Map map;

    private List<Edible> fruits;
    private List<Ghost> ghosts;
    private List<Ghost> removedGhosts;

    private Waka player;
    private int playerLives;
    private PImage livesSprite;
    
    private boolean debugMode;
    private boolean running;
    private boolean win;

    /**
     * Creates an instance of GameManager given an application. Game settings are parsed from
     * the "config.json" file, Fruits, Ghosts, Waka and Walls are adapted from the game Map and
     * modified according to the specified settings.
     * 
     * @param app
     */
    public GameManager(PApplet app) {

        this.settings = new Settings();
        this.settings.parseConfigFile("config.json");
        this.map = new Map(settings.getMapName(), app);

        this.fruits = new ArrayList<Edible>(this.map.getFruits());
        this.ghosts = new ArrayList<Ghost>(this.map.getGhosts());
        this.removedGhosts = new ArrayList<Ghost>();
        this.setupGhosts();
        this.setupPlayer(app);
        
        this.livesSprite = app.loadImage("src/main/resources/playerRight.png");
        this.debugMode = false;
        this.running = true;
        this.win = false;
    }

    /**
     * Continually coordinates between GameObjects and modifies game progress according to object
     * interactions - encapsulates underlying game logic.
     * 
     * @param app
     */
    public void tick(PApplet app) {

        this.playerGhostCollision();
        this.setWallCollision(this.player);
        this.player.tick(app);
        this.eatFruit();

        for (int i = 0; i < this.ghosts.size(); i ++) {
            Ghost ghost = this.ghosts.get(i);

            if (ghost.getState() != GhostState.FRIGHTENED) {
                if (ghost instanceof Whim) {
                    Ghost chaser = null;
                    for (int j = 0; j < this.ghosts.size(); j ++) {
                        if (this.ghosts.get(j) instanceof Chaser) {
                            chaser = this.ghosts.get(j);
                            break;
                        }
                    }
                    if (chaser != null) {
                        ghost.changeTarget(map, chaser);
                    } else {
                        ghost.changeTarget(map, player);
                    }

                } else {
                    ghost.changeTarget(map, player);
                }
            }

            if (ghost.atIntersection(this.map)) {
                ghost.setNextMove(this.map);
            } else {
                if (ghost.atDeadEnd(map)) {
                    ghost.setNextMove(ghost.getOrientation().getOpposite());
                }
            }
            this.setWallCollision(ghost);
            ghost.tick();
        }
    }

    /**
     * Draws in GameObjects, i.e. Walls of the Map, all Fruits, the Waka, Ghosts and remaining 
     * player lives, as well as the debug mode if it has been activated.
     * 
     * @param app
     */
    public void draw(PApplet app) {

        this.map.draw(app);

        for (int i = 0; i < this.fruits.size(); i ++) {
            this.fruits.get(i).draw(app);
        }
        this.player.draw(app);
        
        for (int j = 0; j < this.ghosts.size(); j ++) {
            this.ghosts.get(j).draw(app);
        }

        if (this.debugMode) {
            this.drawDebug(app);
        }
        this.drawLives(app);
    }

    /**
     * Initialises player object and sets initial starting positions and speed.
     * 
     * @param app
     */
    private void setupPlayer(PApplet app) {

        this.playerLives = settings.getPlayerLives();
        int startRow = this.map.getPlayerRow();
        int startCol = this.map.getPlayerCol();
        this.player = new Waka(startRow, startCol, settings.getSpeed(), app);
    }

    /**
     * Sets speed, ghost mode durations and frightened duration for all Ghosts.
     */
    private void setupGhosts() {

        for (int i = 0; i < this.ghosts.size(); i ++) {

            Ghost ghost = this.ghosts.get(i);
            ghost.setSpeed(settings.getSpeed());
            ghost.setGhostMode(settings.getGhostModes());
            ghost.setFrightenedLength(settings.getFrightenedLength());
        }
    }

    /**
     * Interprets relevant key commands into Waka movement or switching in and out of debug mode.
     * 
     * @param key
     * @param app
     */
    public void keyCommands(int key, PApplet app) {

        if (key == 37) {
            this.player.setNextMove(State.LEFT);

        } else if (key == 39) {
            this.player.setNextMove(State.RIGHT);

        } else if (key == 38) {
            this.player.setNextMove(State.UP);

        } else if (key == 40) {
            this.player.setNextMove(State.DOWN);
            
        } else if (key == 32) {
            if (this.debugMode) {
                this.debugMode = false;
            } else {
                this.debugMode = true;
            }
        }
    }

    /**
     * Returns current game status on whether or not it is currently running.
     * 
     * @return true if the game is still running, otherwise returns false.
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Returns whether or not the player has won the game.
     * 
     * @return true if player has won, otherwise returns false for a game over loss.
     */
    public boolean playerWin() {
        return this.win;
    }

    /**
     * Draws sprites of the remaining player lives.
     * 
     * @param app
     */
    private void drawLives(PApplet app) {

        int pixelGap = 28;
        for (int i = 0; i < this.playerLives; i ++) {
            app.image(this.livesSprite, 8 + i * pixelGap, 544);
        }
    }

    /**
     * Checks the status of every in-game Fruit and determines whether or not any have been
     * consumed by the Waka. If a Fruit has been eaten, it will no longer exist on the map and
     * GameManager will check for a win, i.e. if no Fruits remain.
     * 
     * If a SuperFruit is consumed, all ghosts will enter Frightened mode.
     */
    private void eatFruit() {

        for (int i = 0; i < this.fruits.size(); i ++) {
            Edible fruit = this.fruits.get(i);
            
            // Checks if player has eaten any fruits.
            if (this.player.objectCollision(fruit)) {
                this.fruits.remove(fruit);
                
                // Checks whether any fruits remain on the map and award a victory if none remain.
                if (this.fruits.size() == 0) {
                    this.running = false;
                    this.win = true;

                } else if (fruit instanceof SuperFruit) {
                    for (int j = 0; j < this.ghosts.size(); j ++) {
                        this.ghosts.get(j).frightenGhost();
                    }
                }
            }
        }
    }

    /**
     * Draws the debugging line for all Ghosts.
     * 
     * @param app
     */
    private void drawDebug(PApplet app) {

        for (int i = 0; i < this.ghosts.size(); i ++) {
            this.ghosts.get(i).drawDebug(app);
        }
    }

    /**
     * Determines object position relative to the game Walls. Specifies whether it can turn
     * in the direction of its next move and whether or not continued movement will cause it to
     * collide with a Wall.
     */
    public void setWallCollision(Moving object) {

        if (object.getNextMove() != null) {
            boolean canTurn = this.map.checkWallCollision(object, true);
            object.setTurn(!canTurn);
        }
        boolean willCollide = this.map.checkWallCollision(object, false);
        object.setCollision(willCollide);
    }

    /**
     * Checks whether the player Waka has collided with any of the Ghosts. If the Ghosts are
     * currently Frightened, they are removed from the Map. Otherwise, the player loses a life
     * and the Waka and any Ghosts are reset to their initial positions.
     */
    public void playerGhostCollision() {
        
        for (int i = 0; i < this.ghosts.size(); i ++) {
            Ghost ghost = this.ghosts.get(i);
            
            if (this.player.objectCollision(ghost)) {
                
                // Checks the state of the Ghost if there is a collision between it and the Waka.
                // If the Ghost is Frightened, it resumes its previous State and is temporarily 
                // removed from the Map.
                if (ghost.getState() == GhostState.FRIGHTENED) {
                    ghost.resetState();
                    this.ghosts.remove(ghost);
                    this.removedGhosts.add(ghost);
                
                // If the Ghost is not Frightened, the player loses a life and Waka and Ghosts are
                // reset to their starting points.
                } else {
                    this.playerLives -= 1;
                    if (this.playerLives > 0) {
                        this.resetGame();
                    } else {
                        this.running = false;
                    }
                }
            }
        }
    }

    /**
     * Resets the position of the Waka and Ghosts to their initial starting positions. Returns
     * any eaten ghosts to the Map.
     */
    public void resetGame() {

        // Returns any removed Ghosts back to the Map.
        int deadGhosts = this.removedGhosts.size();

        for (int i = 0; i < deadGhosts; i ++) {
            Ghost ghost = this.removedGhosts.get(0);
            this.removedGhosts.remove(ghost);
            this.ghosts.add(ghost);
        }

        // Resets all Ghosts to starting positions.
        for (int j = 0; j < this.ghosts.size(); j ++) {
            this.ghosts.get(j).respawn();
            this.ghosts.get(j).resetPos();
        }
        this.player.resetPos();
    }
}
