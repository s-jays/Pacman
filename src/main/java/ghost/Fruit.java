package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Fruit class defines a subclass of Edible items. Fruits can be eaten, with consumption of all
 * fruits being necessary to win the game.
 */
public class Fruit extends Edible {
    
    /**
     * Creates an instance of Fruit at the given x and y coordinates.
     * 
     * @param x fixed x coordinate
     * @param y fixed y coordinate
     * @param app
     */
    public Fruit(int x, int y, PApplet app) {

        super(x, y);
        this.sprite = app.loadImage("src/main/resources/fruit.png");
    }
}
