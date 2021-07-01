package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

/**
 * App class draws the main body of the game as it runs.
 */
public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    private PFont font;
    private int frameCount;
    boolean showText;

    private GameManager game;

    /**
     * Creates an instance of App.
     */
    public App() {}

    /**
     * Sets up the framerate, font and game managing system to be used in the App.
     */
    public void setup() {

        frameRate(60);

        font = this.createFont("src/main/resources/PressStart2P-Regular.ttf", 44);
        this.textFont(font);
        this.textAlign(CENTER);
        this.frameCount = 0;
        this.showText = true;

        this.game = new GameManager(this);
    }

    /**
     * Defines the size of the App window.
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Draws in all GameObjects as directed by the GameManager.
     */
    public void draw() { 

        background(0, 0, 0);

        // Draws in GameObjects as long as GameSystem is active and running.
        if (this.game.isRunning()) {
            this.game.tick(this);
            this.game.draw(this);

        } else {

            // Draws either game over or victory screen if game has ended.
            if (this.game.playerWin()) {
                this.text("YOU WIN", WIDTH / 2, 240);
            } else {
                this.text("GAME OVER", WIDTH / 2, 240);
            }
            
            // Reloads game after 10 seconds.
            this.frameCount ++;

            if (this.frameCount > 10 * 60) {
                this.setup();

            } else {
                // Draws in remaining seconds until restart
                this.textFont(font, 15);
                String reloadText = "Restarting in... ";
                this.text(reloadText + (10 - this.frameCount / 60), (WIDTH - WIDTH / 3 - 15), 40);
                this.textFont(font);

                // Flashes force restart text
                if (showText) {
                    this.textFont(font, 24);
                    this.textLeading(50);
                    this.text("Press\n[ Spacebar ]\nto restart", WIDTH / 2, 340);
                    this.textFont(font);
                }
                if ((this.frameCount % 30) == 0) {
                    showText = !showText;
                }
            }
        }
        
    }

    /**
     * Either restarts game from Game Over page when spacebar is pressed or passes any received
     * key commands to be processed by the GameManager.
     */
    public void keyPressed() {

        if ((keyCode == 32) && (!this.game.isRunning())) {
            this.setup();
        } else {
            this.game.keyCommands(keyCode, this);
        }
    }

    /**
     * Initiates the Application and game.
     */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}
