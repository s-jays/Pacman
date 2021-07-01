package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Edible class represents the edible game elements, i.e. the Fruit and SuperFruit.
 */
public class Edible extends Fixed {
    
    /**
     * Creates an instance of Edible at the given x and y coordinates.
     * .
     * @param x fixed x coordinate
     * @param y fixed y coordinate
     */
    public Edible(int x, int y) {
        super(x, y);
    }
}
