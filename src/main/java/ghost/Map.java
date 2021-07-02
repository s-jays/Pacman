package ghost;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Map class stores game layout.
 */
public class Map {
    
    private List<Character[]> mapText;
    private Wall[][] loadMap;
    private int rows;
    private int cols;

    private int playerStartRow;
    private int playerStartCol;
    private List<Edible> fruits;
    private List<Ghost> ghosts;

    /**
     * Creates an instance of Map by interpreting a list of characters into corresponding 
     * GameObjects.
     * @param mapText the list of characters parsed from a map text file.
     * @param app
     */
    public Map(List<Character[]> mapText, PApplet app) {

        this.mapText = mapText;
        this.rows = mapText.size();
        this.cols = mapText.get(0).length;
        
        this.playerStartRow = 0;
        this.playerStartCol = 0;
        this.fruits = new ArrayList<Edible>();
        this.ghosts = new ArrayList<Ghost>();

        this.loadMap = this.initMap(app);
    }

    /**
     * Draws in the Walls of the Map.
     * @param app
     */
    public void draw(PApplet app) {
        for (int i = 0; i < this.rows; i ++) {
            for (int j = 0; j < this.cols; j ++) {
                Wall wall = this.loadMap[i][j];
                
                if (wall != null) {
                    wall.draw(app);
                }
            }
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public int getPlayerRow() {
        return this.playerStartRow;
    }

    public int getPlayerCol() {
        return this.playerStartCol;
    }

    public List<Edible> getFruits() {
        return this.fruits;
    }

    public List<Ghost> getGhosts() {
        return this.ghosts;
    }
    
    /**
     * Traverses the list of map characters, assigning them to corresponding GameObjects. Walls
     * are stored in the Map's wall array, the player starting position is set and Fruits and
     * Ghosts are stored in their respective lists.
     * @param app
     * @return the created 2d Wall layout of the Map.
     */
    private Wall[][] initMap(PApplet app) {

        Wall[][] wallArray = new Wall[this.rows][this.cols];
        int numPlayer = 0;
        int numFruit = 0;

        for (int i = 0; i < this.rows; i ++) {
            for (int j = 0; j < this.cols; j ++) {
                Character currentChar = this.mapText.get(i)[j];
                Wall cell = null;

                switch (currentChar) {
                    case '0':
                        break;
                    
                    case '1': case '2': case '3': case '4': case '5': case '6':
                        cell = new Wall(j, i, currentChar, app);
                        break;
                    
                    case '7':
                        fruits.add(new Fruit(j, i, app));
                        numFruit ++;
                        break;

                    case '8':
                        fruits.add(new SuperFruit(j, i, app));
                        numFruit ++;
                        break;

                    case 'p':
                        this.playerStartRow = j;
                        this.playerStartCol = i;
                        numPlayer ++;
                        break;

                    case 'a':
                        ghosts.add(new Ambusher(j, i, 0, app));
                        break;

                    case 'c':
                        ghosts.add(new Chaser(j, i, 0, app));
                        break;
                            
                    case 'i':
                        ghosts.add(new Ignorant(j, i, 0, app));
                        break;
                        
                    case 'w':
                        ghosts.add(new Whim(j, i, 0, app));
                        break;
                }
                wallArray[i][j] = cell;
            }
        }

        if (numPlayer == 0) {
            System.out.println("Error: No player present.");
            System.exit(1);

        } else if (numFruit == 0) {
            System.out.println("Error: No fruit on game map.");
            System.exit(1);
        }
        return wallArray;
    }

    /**
     * Returns true if argument object has 'collided' with any Wall objects, otherwise returns
     * false. Object boundaries are checked against all Wall boundaries and the presence of an
     * overlap is returned.
     * 
     * @param object the object to be checked for collision.
     * @param checkTurn specifies whether to check for collision upon turning or upon continual
     * movement in object's current direction. Argument is true if checking for possible turns,
     * false otherwise.
     * @return
     */
    public boolean checkWallCollision(Moving object, boolean checkTurn) {
        
        // Defines object boundaries.
        int objectLeft = object.getX();
        int objectRight = objectLeft + GameObject.SPRITE_SIZE;
        int objectTop = object.getY();
        int objectBottom = objectTop + GameObject.SPRITE_SIZE;
        
        // Specifies whether the function will check for collision upon turning or collision upon
        // continued movement.
        State move;
        if (checkTurn) {
            move = object.getNextMove();
        } else {
            move = object.orientation;
        }

        switch (move) {
            case LEFT:
                objectLeft -= 1;
                objectRight -= 1;
                break;
            case RIGHT:
                objectLeft += 1;
                objectRight += 1;
                break;
            case UP:
                objectTop -= 1;
                objectBottom -= 1;
                break;
            case DOWN:
                objectTop += 1;
                objectBottom += 1;
                break;
        }

        // Compares object boundaries after possible move to each Wall object and returns true
        // if there is an overlap.
        for (int i = 0; i < this.loadMap.length; i ++) {
            for (int j = 0; j < this.loadMap[i].length; j ++) {
                Wall check = this.loadMap[i][j];
                if (check == null) {
                    continue;
                } else {
                    int wallLeft = check.getX();
                    int wallRight = wallLeft + GameObject.SPRITE_SIZE;
                    int wallTop = check.getY();
                    int wallBottom = wallTop + GameObject.SPRITE_SIZE;

                    if ((objectRight > wallLeft) && (objectLeft < wallRight) && 
                        (objectBottom > wallTop) && (objectTop < wallBottom)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
