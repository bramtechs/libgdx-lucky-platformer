package com.magma.engine.chars;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.magma.engine.collision.Triggered;
import com.magma.engine.entities.Entity;
import com.magma.engine.entities.EntityComponent;
import com.magma.engine.gfx.AnimSlot;
import com.magma.engine.gfx.AnimatedSprite;
import com.magma.engine.utils.MagmaMath;

public abstract class Character extends AnimatedSprite implements Triggered, EntityComponent {

    private enum Direction {
        Up, Down, Right, Left
    }

    protected Entity entity;
    protected AnimSlot animUp, animDown, animSide;
    private int stopFrames;

    private Rectangle lastIntersect;
    private Direction direction;

    public Character(Entity entity, AnimSlot animUp, AnimSlot animDown, AnimSlot animSide) {
       super(animUp, animDown, animSide);
        this.entity = entity;
        this.animUp = animUp;
        this.animDown = animDown;
        this.animSide = animSide;

        setPlayMode(PlayMode.LOOP);
        setCurrentAnimation(animDown);
        direction = Direction.Up;
    }

    public void collisionPush(Rectangle outOf) {
        if (lastIntersect == null) {
            lastIntersect = new Rectangle();
        }
        lastIntersect = new Rectangle();
        Shape2D shape = MagmaMath.extractShape(this);
        if (shape instanceof Rectangle) {
            Intersector.intersectRectangles(outOf, (Rectangle) shape, lastIntersect);
        } else {
            throw new IllegalArgumentException("Characters only support rectangles as collision shape");
        }

        // stop the current animation
        stop();

        float moveX = 0f;
        float moveY = 0f;

        switch (direction) {
            case Down:
                moveY = -lastIntersect.height;
                break;
            case Left:
                moveX = lastIntersect.width;
                break;
            case Right:
                moveX = -lastIntersect.width - 0.01f; // prevent launching bugs for some reason aka speedrun glitch
                break;
            case Up:
                moveY = lastIntersect.height;
                break;
            default:
                throw new IllegalArgumentException("Direction not programmed");
        }

        // make launch bugs look less ridiculous, if they would occur
        moveX = MathUtils.clamp(-5, moveX, 5);
        moveY = MathUtils.clamp(-5, moveY, 5);

        // offset to new position
        // dont use setPosition()!
        setX(getX() + moveX);
        setY(getY() + moveY);
    }

    @Override
    public void moveBy(float x, float y) {
        if (x > 0) { // right
            setFlip(false, false);
            this.setCurrentAnimation(animSide);
            direction = Direction.Right;
        } else if (x < 0) { // left
            setFlip(true, false);
            this.setCurrentAnimation(animSide);
            direction = Direction.Left;
        } else if (y < 0) { // up
            this.setCurrentAnimation(animUp);
            direction = Direction.Up;
        } else if (y > 0) { // down
            this.setCurrentAnimation(animDown);
            direction = Direction.Down;
        }
        stopFrames = 0;
        super.moveBy(x, y);
    }

    @Override
    public void act(float delta) {
        stopFrames++;
        if (stopFrames > 1) {
            this.stop();
        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // if (lastIntersect != null) {
        // Shapes.getInstance().setColor(Color.PURPLE);
        // Shapes.getInstance().filledRectangle(lastIntersect);
        // }
        super.draw(batch, parentAlpha);
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public Shape2D getShape() {
        float feetSize = getRegion().getWidth()*0.8f;
        float height = getRegion().height * 0.5f;
        return new Rectangle(getX() - feetSize*0.5f, getY() - height, feetSize, feetSize);
    }
}
