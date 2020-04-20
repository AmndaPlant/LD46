package com.amnda.ludumdare.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    private Vector2 velocity;
    private Direction currentDirection;
    private Direction previousDirection;

    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;
    private Animation<TextureRegion> walkUp;
    private Animation<TextureRegion> walkDown;

    private Array<TextureRegion> walkLeftFrames;
    private Array<TextureRegion> walkRightFrames;
    private Array<TextureRegion> walkUpFrames;
    private Array<TextureRegion> walkDownFrames;

    private Vector2 nextPosition;
    private Vector2 currentPosition;
    private State state = State.IDLE;
    private float frameTime;
    private Sprite frameSprite;
    private TextureRegion currentFrame;

    private Texture texture;

    private Rectangle boundingBox;

    public enum State {
        IDLE,
        WALKING
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    public Player() {
        this.nextPosition = new Vector2();
        this.currentPosition = new Vector2();
        this.boundingBox = new Rectangle();
        this.velocity = new Vector2(100, 100);
        frameTime = 0;
        currentDirection = Direction.DOWN;
        texture = new Texture("player.png");
        loadSprite();
        loadAnimations();
    }

    private void loadSprite() {
        TextureRegion[][] textureFrames = TextureRegion.split(texture, 32, 32);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, 32, 32);
        currentFrame = textureFrames[0][0];
    }

    private void loadAnimations() {
        TextureRegion[][] textureFrames = TextureRegion.split(texture, 32, 32);
        walkDownFrames = new Array<TextureRegion>(4);
        walkUpFrames = new Array<TextureRegion>(4);
        walkLeftFrames = new Array<TextureRegion>(4);
        walkRightFrames = new Array<TextureRegion>(4);

        for (int i = 0; i < 4; i++) {
            walkDownFrames.insert(i, textureFrames[0][i]);
            walkUpFrames.insert(i, textureFrames[2][i]);
            walkLeftFrames.insert(i,textureFrames[3][i]);
            walkRightFrames.insert(i, textureFrames[1][i]);
        }

        walkDown = new Animation<TextureRegion>(0.1f, walkDownFrames, Animation.PlayMode.LOOP);
        walkUp = new Animation<TextureRegion>(0.1f, walkUpFrames, Animation.PlayMode.LOOP);
        walkLeft = new Animation<TextureRegion>(0.1f, walkLeftFrames, Animation.PlayMode.LOOP);
        walkRight = new Animation<TextureRegion>(0.1f, walkRightFrames, Animation.PlayMode.LOOP);
    }

    public void startingPosition(float x, float y) {
        this.currentPosition.set(x, y);
        this.nextPosition.set(x, y);
    }

    public void update(float delta) {
        if (state == State.WALKING) frameTime += delta;
        else frameTime = 0;
        boundingBox.set(nextPosition.x, nextPosition.y, 15, 15);
    }

    public void setDirection(Direction direction) {
        previousDirection = currentDirection;
        currentDirection = direction;

        switch (currentDirection) {
            case DOWN:
                currentFrame = walkDown.getKeyFrame(frameTime);
                break;
            case UP:
                currentFrame = walkUp.getKeyFrame(frameTime);
                break;
            case LEFT:
                currentFrame = walkLeft.getKeyFrame(frameTime);
                break;
            case RIGHT:
                currentFrame = walkRight.getKeyFrame(frameTime);
                break;
        }
    }

    public void move(Direction direction, float delta) {
        float x = currentPosition.x;
        float y = currentPosition.y;

        switch (direction) {
            case DOWN:
                y -= velocity.y * delta;
                break;
            case UP:
                y += velocity.y * delta;
                break;
            case LEFT:
                x -= velocity.x * delta;
                break;
            case RIGHT:
                x += velocity.x * delta;
                break;
        }

        nextPosition.x = x;
        nextPosition.y = y;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Sprite getFrameSprite() {
        return frameSprite;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void setCurrentPosition(float x, float y) {
        frameSprite.setX(x);
        frameSprite.setY(y);
        this.currentPosition.x = x;
        this.currentPosition.y = y;
        this.nextPosition.x = x;
        this.nextPosition.y = y;
    }

    public void setCurrentToNext() {
        setCurrentPosition(nextPosition.x, nextPosition.y);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public float getPlayerCentreX() {
        return boundingBox.x + boundingBox.width / 2;
    }

    public float getPlayerCentreY() {
        return boundingBox.y + boundingBox.height / 2;
    }
}
