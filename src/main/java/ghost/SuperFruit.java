package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * SuperFruit class defines a subclass of Edible items. SuperFruit objects have the ability
 * to 'frighten' ghosts and allow the player a temporary advantage. All SuperFruit objects
 * must be eaten alongside Fruit objects to achieve victory.
 */
public class SuperFruit extends Edible {
    
    /**
     * Creates an instance of SuperFruit, given an x and y position, and an application.
     * @param x
     * @param y
     * @param app
     */
    public SuperFruit(int x, int y, PApplet app) {
        super(x, y);
        this.sprite = app.loadImage("src/main/resources/superFruit.png");
    }
}