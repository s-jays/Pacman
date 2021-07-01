package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Whim Class represents the Whim type Ghost.
 */
public class Whim extends Ghost {

    /**
     * Creates an instance of Whim at the given x and y starting positions and movement speed.
     * 
     * @param x Whim starting x position
     * @param y Whim starting y position
     * @param speed Whim movement speed
     * @param app
     */
    public Whim(int x, int y, int speed, PApplet app) {
        
        super(x, y, speed, app);
        this.sprite = app.loadImage("src/main/resources/whim.png");
        this.normalSprite = this.sprite;
        this.orientation = State.RIGHT;
    }
    
    /**
     * In Scatter mode, Whim targets bottom right corner of the Map. In Chase mode, Whim
     * targets the grid space that is twice the distance of the vector from Chaser to 2 spaces
     * ahead of Waka's current position.
     */
    public void changeTarget(Map map, Moving object) {

        if (this.state == GhostState.SCATTER) {
            int right = map.getCols() * GameObject.SPRITE_SIZE;
            int bottom = map.getRows() * GameObject.SPRITE_SIZE;
            this.setTarget(right, bottom);

        } else {
            try {
                Ghost chaser = (Ghost)object;
                setWhimTarget(map, chaser);

            } catch (ClassCastException e) {
                int targetX = object.getX() + GameObject.SPRITE_SIZE / 2;
                int targetY = object.getY() + GameObject.SPRITE_SIZE / 2;

                if (targetX < 0) {
                    targetX = 0;
                } else if (targetX > map.getCols() * GameObject.SPRITE_SIZE) {
                    targetX = map.getCols() * GameObject.SPRITE_SIZE;
                }
                if (targetY < 0) {
                    targetY = 0;
                } else if (targetY > map.getRows() * GameObject.SPRITE_SIZE) {
                    targetY = map.getRows() * GameObject.SPRITE_SIZE;
                }
                this.setTarget(targetX, targetY);
            }
        }
    }

    public void setWhimTarget(Map map, Ghost chaser) {

        int chaserTargetX = chaser.getTargetX();
        int chaserTargetY = chaser.getTargetY();
        int chaserX = chaser.getX();
        int chaserY = chaser.getY();

        int targetX = 2 * chaserTargetX - chaserX;
        int targetY = 2 * chaserTargetY - chaserY;

        if (targetX < 0) {
            targetX = 0;
        } else if (targetX > map.getCols() * GameObject.SPRITE_SIZE) {
            targetX = map.getCols() * GameObject.SPRITE_SIZE;
        }
        if (targetY < 0) {
            targetY = 0;
        } else if (targetY > map.getRows() * GameObject.SPRITE_SIZE) {
            targetY = map.getRows() * GameObject.SPRITE_SIZE;
        }
        this.setTarget(targetX, targetY);
    }
}
//TODO - SET LIMITS/OUT OF RANGE LIMITS FOR ALL GHOSTS - potentially set up error checking in Ghost itself?
// public int setXLimit() and setYLimit()
