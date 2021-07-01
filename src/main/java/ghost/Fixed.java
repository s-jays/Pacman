package ghost;

import processing.core.PApplet;

/**
 * Fixed class represents all immoveable game elements, i.e. Walls and Edible items.
 */
public class Fixed extends GameObject {
    
    /**
     * Creates an instance of Fixed at tge given x and y coordinates.
     * 
     * @param x fixed x coordinate
     * @param y fixed y coordinate
     */
    public Fixed(int x, int y) {
        super(x, y);
    }

    /**
     * Draws in the PImage sprite of the object.
     * @param app, the application to be drawn into.
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }
}
