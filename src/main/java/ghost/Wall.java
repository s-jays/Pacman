package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * WallType enumeration defines the direction of a Wall object.
 */
enum WallType {
    HORIZONTAL,
    VERTICAL,
    UPLEFT,
    UPRIGHT,
    DOWNLEFT,
    DOWNRIGHT;

    /**
     * Returns the corresponding WallType to a specified character argument.
     * 
     * @param character the character to be translated into a WallType.
     * @return the WallType equivalent of the character.
     */
    public WallType charToWall(char character) {

        switch (character) {
            case '1':
                return HORIZONTAL;
            case '2':
                return VERTICAL;
            case '3':
                return UPLEFT;
            case '4':
                return UPRIGHT;
            case '5':
                return DOWNLEFT;
            case '6':
                return DOWNRIGHT;
            default:
                return null;
        }
    }
}

/**
 * Wall class represents the game boundaries and forms the game layout.
 */
public class Wall extends Fixed {
    
    private WallType orientation;

    /**
     * Creates an instance of Wall at the given x and y positions
     * @param x Wall x position
     * @param y Wall y position
     * @param character character corresponding to WallType
     * @param app
     */
    public Wall(int x, int y, char character, PApplet app) {

        super(x, y);
        this.sprite = app.loadImage("src/main/resources/horizontal.png");
        this.setSprite(character, app);
    }

    /**
     * Sets Wall object's WallType sprite and orientation according to the
     * character representation
     * 
     * @param character character corresponding to a WallType.
     * @param app
     */
    private void setSprite(char character, PApplet app) {
        
        // Sets WallType to Horizontal and sprite to null by default.
        this.orientation = WallType.HORIZONTAL;
        PImage sprite = null;
        
        // Sets Wall orientation according to its character representation.
        this.orientation = this.orientation.charToWall(character);

        switch (this.orientation) {
            case HORIZONTAL:
                sprite = app.loadImage("src/main/resources/horizontal.png");
                break;
            case VERTICAL:
                sprite = app.loadImage("src/main/resources/vertical.png");
                break;
            case UPLEFT:
                sprite = app.loadImage("src/main/resources/upLeft.png");
                break;
            case UPRIGHT:
                sprite = app.loadImage("src/main/resources/upRight.png");
                break;
            case DOWNLEFT:
                sprite = app.loadImage("src/main/resources/downLeft.png");
                break;
            case DOWNRIGHT:
                sprite = app.loadImage("src/main/resources/downRight.png");
                break;
        }
        this.sprite = sprite;
    }
}
