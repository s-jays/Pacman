package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Chaser Class represents the Chaser type Ghost.
 */
public class Chaser extends Ghost {

    /**
     * Creates an instance of Chaser with the given x and y starting coordinates and movement 
     * speed.
     * 
     * @param x starting x coordinate
     * @param y starting y coordinate 
     * @param speed movement speed
     * @param app
     */
    public Chaser(int x, int y, int speed, PApplet app) {
        
        super(x, y, speed, app);
        this.sprite = app.loadImage("src/main/resources/chaser.png");
        this.normalSprite = this.sprite;
    }

    /**
     * In Scatter mode, Chaser targets top left corner of the Map. In Chase mode, Chaser targets
     * Waka's current coordinate.
     */
    public void changeTarget(Map map, Moving object) {

        int xTarget = 0;
        int yTarget = 0;

        if (this.state == GhostState.CHASE) {
            xTarget = object.getX() + GameObject.SPRITE_SIZE / 2;
            yTarget = object.getY() + GameObject.SPRITE_SIZE / 2;
        }
        xTarget = setXLimit(map, xTarget);
        yTarget = setYLimit(map, yTarget);
        this.setTarget(xTarget, yTarget);
    }
}
