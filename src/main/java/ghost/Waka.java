package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Waka class represents game's controllable player object.
 */
public class Waka extends Moving {

    private PImage currDirection;
    private PImage closedSprite;
    private PImage leftSprite;
    private PImage rightSprite;
    private PImage upSprite;
    private PImage downSprite;

    /**
     * Creates an instance of Waka, at the given x and y positions and 
     * movement speed.
     * 
     * @param x starting x position
     * @param y starting y position
     * @param speed movement speed
     * @param app
     */
    public Waka(int x, int y, int speed, PApplet app) {
        
        super(x, y, speed);
        this.sprite = app.loadImage("src/main/resources/playerLeft.png");
        this.currDirection = this.sprite;

        this.closedSprite = app.loadImage("src/main/resources/playerClosed.png");
        this.leftSprite = app.loadImage("src/main/resources/playerLeft.png");
        this.rightSprite = app.loadImage("src/main/resources/playerRight.png");
        this.upSprite = app.loadImage("src/main/resources/playerUp.png");
        this.downSprite = app.loadImage("src/main/resources/playerDown.png");
    }

    /**
     * Commands Waka movement and sprite logic. If Waka is able to turn, it will turn. If Waka
     * will not collide with a wall upon further movement, Waka will move. Waka sprite state 
     * switches every 8 frames.
     * 
     * @param app
     */
    public void tick(PApplet app) {

        if ((app.frameCount + 4) % 8 == 0) {
            this.switchSprite();
        }
        this.turn();

        if (!this.willCollide) {
            this.orientation.move(this);
        }
    }
    
    /**
     * Switches Waka's current sprite between its open and closed states.
     */
    public void switchSprite() {
        if (this.sprite == this.closedSprite) {
            this.sprite = this.currDirection;
        } else {
            this.sprite = this.closedSprite;
        }
    }

    /**
     * Turns Waka in the direction of its next move if available.
     */
    private void turn() {
        
        if (this.canTurn) {
            if (this.nextMove != null) {
                this.orientation = this.nextMove;
                
                // Switches Waka sprites according to its new orientation.
                switch (this.orientation) {
                    case LEFT:
                        this.currDirection = this.leftSprite;
                        break;
                    case RIGHT:
                        this.currDirection = this.rightSprite;
                        break;
                    case UP:
                        this.currDirection = this.upSprite;
                        break;
                    case DOWN:
                        this.currDirection = this.downSprite;
                        break;
                }
                this.nextMove = null;
            }
        }
        
    }
}