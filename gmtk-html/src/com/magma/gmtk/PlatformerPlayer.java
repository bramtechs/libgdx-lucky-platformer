package com.magma.gmtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.magma.engine.assets.Shapes;
import com.magma.engine.collision.Triggered;
import com.magma.engine.maps.triggers.CustomTrigger;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlatformerPlayer extends Actor implements Triggered, CustomTrigger {

    private enum Direction {
        Up, Down, Right, Left
    }

    public static float gravity = 10f;
    private static final float CAP = 50f;
    private static final float JUMP = 10f;
    public static PlatformerPlayer instance;

    protected ShapeDrawer shapes;
    private float velX;
    private float velY;

    private Direction direction;
    private PlatformerStage stage;
    private GridPoint2 floor;
    private static boolean debugDraw;
	public static boolean died;

    public PlatformerPlayer(PlatformerStage stage, int x, int y) {
        this.stage = stage;
        died = false;
        gravity = 10f;
        instance = this;
        shapes = Shapes.getInstance();
        setPosition(x, y);
        setSize(1f, 1f);
        direction = Direction.Up;
        floor = new GridPoint2();
    }

    @Override
    public void act(float delta) {
        float dir = 0f;
        if (Gdx.input.isKeyPressed(Keys.A)) {
            dir -= 1f;
            direction = Direction.Left;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            dir += 1f;
            direction = Direction.Right;
        }

        floor.set(Math.round(getX()), Math.round(getY() - (int) Math.signum(gravity)));

        if (stage.isSolid(floor.x, floor.y) && Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            velY += JUMP * Math.signum(gravity);
            direction = Direction.Right;
            PlatformerAudio.jump.play();
        }

        velX += dir;
        velY -= gravity * delta;

        // cap
        velX = MathUtils.clamp(velX, -CAP, CAP);
        velY = MathUtils.clamp(velY, -CAP, CAP);

        moveBy(velX * delta, velY * delta);
        toFront();

        // resolve collisions

        // check surrounding tiles
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                int xx = MathUtils.round(getX()) + x;
                int yy = MathUtils.round(getY()) + y;
                if (stage.isSolid(xx, yy)) {
                    shapes.setColor(Color.RED);
                    // get intersecting rect
                    Rectangle inter = getIntersection(xx, yy);
                    if (inter.area() == 0) {
                        continue;
                    }
                    if (inter.width > inter.height) {
                        if (velY < 0) { // falling
                            moveBy(0f, inter.height);
                        } else {
                            moveBy(0f, -inter.height);
                        }
                        velY = 0f;
                    } else {
                        if (velX < 0) { // going left
                            moveBy(inter.width, 0f);
                        } else {
                            moveBy(-inter.width, 0f);
                        }
                        velX = 0f;
                    }
                }
            }
        }

        // check if died
        if (getY() < -1f || getY() > stage.getHeight() + 1f) {
            GmtkGame.restart();
        }

        // check if won
        if (getX() < -1 || getX() > stage.getWidth() + 1) {
            GmtkGame.nextLevel();
        }

        // hidden test level
        if (PlatformerStage.isCheating && Gdx.input.isKeyJustPressed(Keys.F9)) {
            GmtkGame.loadLevel(-1);
        }
        // draw debug test level
        if (PlatformerStage.isCheating && Gdx.input.isKeyJustPressed(Keys.F10)) {
            debugDraw = !debugDraw;
        }

        // MagmaLogger.log(this, getX() + " " + getY());
        super.act(delta);
    }

    public Rectangle getIntersection(int x, int y) {
        Rectangle pr = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Rectangle tr = new Rectangle(x, y, 1f, 1f);
        Rectangle inter = new Rectangle();
        Intersector.intersectRectangles(pr, tr, inter);
        return inter;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setRectColor();
        drawRect();
        if (debugDraw) {
            // check surrounding tiles
            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    int xx = MathUtils.round(getX()) + x;
                    int yy = MathUtils.round(getY()) + y;
                    if (stage.isSolid(xx, yy)) {
                        shapes.setColor(Color.RED);
                        shapes.rectangle(xx, yy, 1f, 1f, 0.1f);
                        // get intersecting rect
                        Rectangle inter = getIntersection(xx, yy);
                        shapes.setColor(Color.PURPLE);
                        shapes.filledRectangle(inter);
                    } else {
                        shapes.setColor(Color.GREEN);
                        shapes.rectangle(xx, yy, 1f, 1f, 0.1f);
                    }
                }
            }

            shapes.setColor(Color.PINK);
            shapes.rectangle(floor.x, floor.y, 1f, 1f, 0.1f);
        }
        super.draw(batch, parentAlpha);
    }

    public void setRectColor() {
        shapes.setColor(PlatformerStage.activePalette.getPlayerColor());
    }

    public void drawRect() {
        shapes.filledRectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public Shape2D getShape() {
        return new Rectangle(getX() - 0.01f, getY() - 0.01f, getWidth() + 0.02f, getHeight() + 0.02f);
    }

    public static void explode() {
        instance.remove();
        died = true;
        PlatformerAudio.explosion.play();
    }

    public void launch(float x, float y){
        velY += y;
        velX += x;
    }
}
