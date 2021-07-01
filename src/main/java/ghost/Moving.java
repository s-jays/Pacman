package ghost;

import processing.core.PApplet;

/**
 * State enumeration defines the direction a Moving object is currently facing. States correspond
 * to the cardinal directions.
 */
enum State {
    LEFT {
        public State getOpposite() {
            return RIGHT;
        }
        public void move(Moving object) {
            object.x -= object.speed;
            object.xDraw -= object.speed;
        }
    },
    RIGHT {
        public State getOpposite() {
            return LEFT;
        }
        public void move(Moving object) {
            object.x += object.speed;
            object.xDraw += object.speed;
        }
    },
    UP {
        public State getOpposite() {
            return DOWN;
        }
        public void move(Moving object) {
            object.y -= object.speed;
            object.yDraw -= object.speed;
        }
    },
    DOWN {
        public State getOpposite() {
            return UP;
        }
        public void move(Moving object) {
            object.y += object.speed;
            object.yDraw += object.speed;
        }
    };

    public abstract State getOpposite();
    public abstract void move(Moving object);
}

public class Moving extends GameObject {
    
    /**
     * Upon being drawn, Moving objects are offset by a value of 4 pixels horizontally.
     */
    public static final int X_OFFSET = 4;

    /**
     * Upon being drawn, Moving objects are offset by a value of 5 pixels vertically.
     */
    public static final int Y_OFFSET = 5;
    
    /**
     * Stores starting x position of Moving object.
     */
    protected int xStart;

    /**
     * Stores starting y position of Moving object.
     */
    protected int yStart;

    /**
     * Stores x position the object will be drawn in.
     */
    protected int xDraw;

    /**
     * Stores y position the object will be drawn in.
     */
    protected int yDraw;

    /**
     * Stores object's next move.
     */
    protected State nextMove;

    /**
     * Stores direction the object is currently facing.
     */
    protected State orientation;

    /**
     * Stores speed of the object.
     */
    protected int speed;

    /**
     * Specifies whether or not the object is able to turn in its current position.
     */
    protected boolean canTurn;

    /**
     * Specifies whether or not the object will collide with a wall upon further movement.
     */
    protected boolean willCollide;

    /**
     * Creates an instance of Moving, given an x and y position and a movement speed.
     * @param x
     * @param y
     * @param speed, the speed at which the object is able to move.
     */
    public Moving(int x, int y, int speed) {
        super(x, y);

        this.xDraw = this.x - Moving.X_OFFSET;
        this.yDraw = this.y - Moving.Y_OFFSET;
        this.xStart = this.x;
        this.yStart = this.y;

        // As a default, all Moving objects will begin facing left.
        this.orientation = State.LEFT;
        this.speed = speed;
        this.canTurn = true;
        this.willCollide = false;
    }

    /**
     * Draws in the offset PImage sprite of the Moving object.
     * @param app
     */
    public void draw(PApplet app) {
        app.image(this.sprite, this.xDraw, this.yDraw);
    }

    /**
     * Resets the current x and y positions of the Moving object to their original values.
     */
    public void resetPos() {
        this.x = this.xStart;
        this.y = this.yStart;
        this.xDraw = this.x - Moving.X_OFFSET;
        this.yDraw = this.y - Moving.Y_OFFSET;
    }

    public State getOrientation() {
        return this.orientation;
    }

    public State getNextMove() {
        return this.nextMove;
    }

    public void setNextMove(State direction) {
        this.nextMove = direction;
    }

    public void setTurn(boolean validTurn) {
        this.canTurn = validTurn;
    }

    public void setCollision(boolean collision) {
        this.willCollide = collision;
    }
}