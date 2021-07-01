package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * GameObject class is the root of the hierarchy, representing all in-game elements.
 */
public class GameObject {
    
    /**
     * The sprite size of all Game Objects is defined to be 16 pixels.
     */
    public static final int SPRITE_SIZE = 16;
    
    /**
     * Stores the position of the object relative to the horizontal axis.
     */
    protected int x;

    /**
     * Stores the position of the object relative to the vertical axis.
     */
    protected int y;

    /**
     * Stores the object's current sprite image.
     */
    protected PImage sprite;

    /**
     * Creates an instance of GameObject given an x and y position on the grid.
     * 
     * @param x, the object's horizontal position.
     * @param y, the object's vertical position.
     */
    public GameObject(int x, int y) {

        this.x = x * GameObject.SPRITE_SIZE;
        this.y = y * GameObject.SPRITE_SIZE;
    }

    /**
     * Get GameObject's x coordinate.
     * 
     * @return object's x coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get GameObject's y coordinate.
     * 
     * @return object's y coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get GameObject's sprite image.
     * 
     * @return object's sprite image.
     */
    public PImage getSprite() {
        return this.sprite;
    }

    /**
     * Returns whether an argument object has 'collided' with the provided GameObject object.
     * 
     * @param object, The object to be checked against.
     * @return true if the object boundaries have overlapped and hence, collided, and false 
     * otherwise
     */
    public boolean objectCollision(GameObject object) {

        // Defines the pixel boundaries of the current object.
        int currLeft = this.x;
        int currTop = this.y;
        int currCentreX = currLeft + GameObject.SPRITE_SIZE / 2;
        int currCentreY = currTop + GameObject.SPRITE_SIZE / 2;

        // Defines the pixel boundaries of the argument object.
        int objLeft = object.getX();
        int objRight = objLeft + GameObject.SPRITE_SIZE;
        int objTop = object.getY();
        int objBottom = objTop + GameObject.SPRITE_SIZE;

        // Checks for overlaps in the objects' boundaries.
        if ((currCentreX <= objRight) && (currCentreX >= objLeft) && 
            (currCentreY <= objBottom) && (currCentreY >= objTop)) {
                return true;
        }
        return false;
    }
}
