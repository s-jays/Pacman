package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Ignorant Class represents the Ignorant type Ghost.
 */
public class Ignorant extends Ghost {

    /**
     * Creates an instance of Ignorant, given x and y starting positions and a movement speed.
     * @param x
     * @param y
     * @param speed
     * @param app
     */
    public Ignorant(int x, int y, int speed, PApplet app) {
        super(x, y, speed, app);
        this.sprite = app.loadImage("src/main/resources/ignorant.png");
        this.normalSprite = this.sprite;
    }

    /**
     * In Scatter mode, Ignorant targets bottom left corner of the Map. In Chase mode, Ignorant
     * targets Waka's current position if its straight line distance towards Waka is over 8 units.
     * Otherwise, Ignorant will target the bottom left corner of the Map.
     */
    public void changeTarget(Map map, Moving object) {
        
        if (this.state == GhostState.SCATTER) {
            this.setTarget(0, map.getRows() * GameObject.SPRITE_SIZE);
        } else {
            int playerX = object.getX();
            int playerY = object.getY();
            int xDist = this.x - playerX;
            int yDist = this.y - playerY;
            double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
            
            // Checks whether straight line distance between player position and Ghost is more than
            // 8 units. If distance is over 8, target is Waka, otherwise target is map bottom left.
            if (distance / GameObject.SPRITE_SIZE - 1 > 8) {
                int targetX = playerX + GameObject.SPRITE_SIZE / 2;
                int targetY = playerY + GameObject.SPRITE_SIZE / 2;
                this.setTarget(targetX, targetY);
            } else {
                this.setTarget(0, map.getPlayerRow() * GameObject.SPRITE_SIZE);
            }
        }
    } 
}