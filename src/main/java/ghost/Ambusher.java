package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Ambusher Class represents the Ambusher type Ghost.
 */
public class Ambusher extends Ghost {

    /**
     * Creates an instance of Ambusher with the given x and y starting coordinates and movement
     * speed.
     * 
     * @param x starting x coordinate
     * @param y starting y coordinate
     * @param speed movement speed
     * @param app
     */
    public Ambusher(int x, int y, int speed, PApplet app) {

        super(x, y, speed, app);
        this.sprite = app.loadImage("src/main/resources/ambusher.png");
        this.normalSprite = this.sprite;
        this.orientation = State.RIGHT;
        this.nextMove = State.RIGHT;
    }

    /**
     * In Scatter mode, Ambusher targets top right corner of the Map. In Chase mode, Ambusher
     * targets the grid coordinate four spaces ahead of Waka's current direction.
     */
    public void changeTarget(Map map, Moving object) {
        
        int targetX = object.getX();
        int targetY = object.getY();
        
        if (this.state == GhostState.SCATTER) {
            targetX = map.getCols() * GameObject.SPRITE_SIZE;
            targetY = 0;

        } else {            
            // Alters target x and y values according to the Waka's orientation in order to get
            // the block four spaces ahead.
            // If either the target x or y go out of bounds, they are set to the most extreme valid
            // value.
            State direction = object.getOrientation();

            switch (direction) {
                case LEFT:
                    targetX -= 4 * GameObject.SPRITE_SIZE;
                    if (targetX < 0) {
                        targetX = 0;
                    }
                    break;

                case RIGHT:
                    targetX += 4 * GameObject.SPRITE_SIZE;
                    int xLimit = map.getCols() * GameObject.SPRITE_SIZE;
                    if (targetX > xLimit) {
                        targetX = xLimit;
                    }
                    break;
                    
                case UP:
                    targetY -= 4 * GameObject.SPRITE_SIZE;
                    if (targetY < 0) {
                        targetY = 0;
                    }
                    break;

                case DOWN:
                    targetY += 4 * GameObject.SPRITE_SIZE;
                    int yLimit = map.getRows() * GameObject.SPRITE_SIZE;
                    if (targetY > yLimit) {
                        targetY = yLimit;
                    }
                    break;
            }
            // Offsets the target x and y to indicate the centre of the Waka cell
            targetX += GameObject.SPRITE_SIZE / 2;
            targetY += GameObject.SPRITE_SIZE / 2;
        }
        this.setTarget(targetX, targetY);
    }
}
