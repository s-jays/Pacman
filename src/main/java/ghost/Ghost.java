package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * GhostState enumeration defines the three possible Ghost modes: Scatter, Chase and Frightened.
 */
enum GhostState {
    SCATTER {
        public GhostState switchState() {
            return CHASE;
        }
    },
    CHASE {
        public GhostState switchState() {
            return SCATTER;
        }
    },
    FRIGHTENED {
        public GhostState switchState() {
            return SCATTER;
        }
    };

    /**
     * Upon being called, the function returns a GhostState switched according to the current one.
     * 
     * @return Scatter will return Chase, Chase will return Scatter and Frightened will return 
     * Scatter.
     */
    public abstract GhostState switchState();
}

/**
 * Ghost class is a subtype of Moving that encapsulates all Ghost types, i.e. Ambusher, Chaser
 * Ignorant and Whim.
 */
public abstract class Ghost extends Moving {

    public static final int GHOST_X_OFFSET = 2;

    /**
     * Stores duration of the Scatter and Chase ghost modes.
     */
    protected int[] ghostModes;

    /**
     * Stores duration of Frightened mode.
     */
    protected int frightenedLength;

    /**
     * Stores the non-frightened, default Ghost sprite.
     */
    protected PImage normalSprite;

    /**
     * Stores the frightened Ghost sprite.
     */
    protected PImage frightenedSprite;

    /**
     * Monitors the current ghost mode iteration.
     */
    protected int currentMode;

    /**
     * Stores the current frame count.
     */
    protected int frameCount;

    /**
     * Stores the duration Ghost is to be frightened for.
     */
    protected int frightenedCount;

    /**
     * Stores the current state of the Ghost, i.e. Scatter, Chase or Frightened.
     */
    protected GhostState state;

    /**
     * Stores the x position of the Ghost target.
     */
    protected int xTarget;

    /**
     * Stores the y position of the Ghost target.
     */
    protected int yTarget;

    /**
     * Creates an instance of Ghost, given starting x and y positions and a movement speed.
     * 
     * @param x
     * @param y
     * @param speed
     * @param app
     */
    public Ghost(int x, int y, int speed, PApplet app) {
        
        super(x, y, speed);
        
        this.xDraw -= Ghost.GHOST_X_OFFSET;
        this.currentMode = 0;
        this.frameCount = 0;
        this.frightenedCount = 0;
        this.state = GhostState.SCATTER;
        this.frightenedSprite = app.loadImage("src/main/resources/frightened.png");

        this.xTarget = 0;
        this.yTarget = 0;

        this.canTurn = false;
    }

    /**
     * Controls all GhostState and movement logic.
     */
    public void tick() {

        if (this.state == GhostState.FRIGHTENED) {
            this.frightenedCount --;
            
            if (this.frightenedCount == 0) {
                // Once the time the Ghost has remained for has surpassed the specified Frightened
                // duration, the frightenedCount and Ghost state will be reset to the previous mode
                this.resetState();
            }

        } else {
            this.frameCount ++;
            
            if (this.frameCount >= this.ghostModes[this.currentMode] * 60) {
                // Once the Ghost has remained in its current mode for a sufficient amount of
                // time, it will switch to the next mode.
                this.frameCount = 0;
                this.currentMode ++;
                this.currentMode %= this.ghostModes.length;
                this.state = this.state.switchState();
            }    
        }
        this.turn();
        this.orientation.move(this);
    }

    public GhostState getState() {
        return this.state;
    }

    public int getTargetX() {
        return this.xTarget;
    }

    public int getTargetY() {
        return this.yTarget;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    protected void setTarget(int x, int y) {
        this.xTarget = x;
        this.yTarget = y;
    }

    public void setGhostMode(int[] modes) {
        this.ghostModes = modes;
    }

    public void setFrightenedLength(int length) {
        this.frightenedLength = length;
    }

    /**
     * Determines whether or not Ghost is at an intersection on the Map.
     * 
     * @param map the game map
     * @return true if Ghost is at an intersection; false otherwise.
     */
    public boolean atIntersection(Map map) {

        State current = this.orientation;
        State opposite = this.orientation.getOpposite();
        State prevNext = this.nextMove;
        int canTurn = 0;

        for (State state : State.values()) {
            if ((state != current) && (state != opposite)) {
                this.nextMove = state;
                if (!map.checkWallCollision(this, true)) {
                    canTurn ++;
                }
            }
        }
        this.nextMove = prevNext;
        return (canTurn >= 1);
    }

    /**
     * Determines whether or not Ghost is at a dead end, i.e. can only move backwards.
     * 
     * @param map the game map.
     * @return true if Ghost is at a dead end; false otherwise.
     */
    public boolean atDeadEnd(Map map) {

        State current = this.orientation;
        State prevNext = this.nextMove;

        this.nextMove = current;
        boolean atDeadEnd = map.checkWallCollision(this, true);
        this.nextMove = prevNext;
        return atDeadEnd;
    }
    
    /**
     * Turns the Ghost in the direction of the next move if available.
     */
    private void turn() {
        if (this.nextMove != null) {
            this.orientation = this.nextMove;
            this.nextMove = null;
        }
    }

    /**
     * Sets the Ghost's current state to Frightened and alter the sprite to reflect the change.
     */
    public void frightenGhost() {
        this.state = GhostState.FRIGHTENED;
        this.frightenedCount = this.frightenedLength * 60;
        this.sprite = this.frightenedSprite;
    }

    /**
     * Draws a white line from the Ghost to its target position in Debug mode
     * @param app
     */
    public void drawDebug(PApplet app) {
        int offset = GameObject.SPRITE_SIZE / 2;
        app.stroke(255);
        app.line(this.xDraw + offset, this.yDraw + offset, this.xTarget, this.yTarget);
    }
    
    /**
     * Switches GhostState back to its previous State.
     */
    public void resetState() {

        if (this.currentMode % 2 == 0) {
            this.state = GhostState.SCATTER;
        } else {
            this.state = GhostState.CHASE;
        }
        this.sprite = this.normalSprite;
        // TODO
    }

    /**
     * Calculates and sets the Ghost's next move to be the one that will enable it to move
     * closest to its target.
     * @param map
     */
    protected void modeMovement(Map map) {

        State opposite = this.orientation.getOpposite();
        State next = opposite;
        double minDist = -1;

        for (State state : State.values()) {
            if (state != opposite) {
                this.nextMove = state;

                if (!map.checkWallCollision(this, true)) {
                    
                    double moveDist = this.getDistance();
                    if (next == opposite) {
                        minDist = moveDist;
                        next = state;

                    } else {
                        if (moveDist < minDist) {
                            minDist = moveDist;
                            next = state;
                        }
                    }
                }
            }
        }
        this.nextMove = next;
    }

    private double getDistance() {

        int xDist = 0;
        int yDist = 0;

        switch (this.nextMove) {
            case LEFT:
                xDist = (this.x - GameObject.SPRITE_SIZE) - this.xTarget;
                yDist = this.y - this.yTarget;
                break;
            case RIGHT:
                xDist = (this.x + GameObject.SPRITE_SIZE) - this.xTarget;
                yDist = this.y - this.yTarget;
                break;
            case UP:
                xDist = this.x - this.xTarget;
                yDist = (this.y - GameObject.SPRITE_SIZE) - this.yTarget;
                break;
            case DOWN:
                xDist = this.x - this.xTarget;
                yDist = (this.y + GameObject.SPRITE_SIZE) - this.yTarget;
                break;
        }
        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
    }

    protected int setXLimit(Map map, int xTarget) {
        
        if (xTarget < 0) {
            xTarget = 0;
        } else {
            int xLimit = map.getCols() * GameObject.SPRITE_SIZE;
            if (xTarget > xLimit) {
                xTarget = xLimit;
            }
        }
        return xTarget;
    }

    protected int setYLimit(Map map, int yTarget) {
        
        if (yTarget < 0) {
            yTarget = 0;
        } else {
            int yLimit = map.getRows() * GameObject.SPRITE_SIZE;
            if (yTarget > yLimit) {
                yTarget = yLimit;
            }
        }
        return yTarget;
    }

    /**
     * Changes the Ghost target. Target varies for each type of Ghost.
     * @param map
     * @param object
     */
    public abstract void changeTarget(Map map, Moving object);
}
