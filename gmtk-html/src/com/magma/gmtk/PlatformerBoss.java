package com.magma.gmtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.magma.engine.assets.Shapes;
import com.magma.engine.chars.CharacterController.Direction;
import com.magma.engine.collision.TriggerListener;
import com.magma.engine.debug.MagmaLogger;
import com.magma.engine.utils.Time;
import com.magma.engine.utils.tweens.Tween;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlatformerBoss extends Actor implements TriggerListener {

    private static final float RADIUS = 3f;
    private static final int PIECES = 40;

    private static boolean hasStarted;

    private PlatformerStage stage;
    private ShapeDrawer shapes;
    private Color[] colors;

    private Vector2[] bodyLocal;
    private Vector2 center;
    private Vector2 spawn;
    private float health = 100f;

    private Tween tweenX;
    private Tween tweenY;

    private float nextMove;
    private Vector2 target;

    private int mode;
    private float modeTimer;
    private float shootTimer;
    private float shootRapidTimer;
    private float printTimer;
    private long hurtTime;

    public PlatformerBoss(PlatformerStage stage, int x, int y) {
        this.stage = stage;
        this.shapes = Shapes.getInstance();
        this.setPosition(x, y);
        this.setSize(1f, 1f);
        spawn = new Vector2(x, y);
        target = new Vector2(x, y);

        if (!hasStarted) {
            hasStarted = true;
            PlatformerAudio.playBossMusic(); // why do I hear boss music?
        }

        bodyLocal = new Vector2[PIECES];
        colors = new Color[PIECES];
        for (int i = 0; i < PIECES; i++) {
            bodyLocal[i] = new Vector2();
            colors[i] = new Color();
        }
        center = new Vector2();
        tweenX = new Tween();
        tweenY = new Tween();

        nextMove = 2f;
        modeTimer = 3f;
    }

    @Override
    public void onTriggered(Actor a) {
        if (a instanceof PlatformerPlayer) {
            if (!PlatformerPlayer.died && Time.getTimeMillis()-hurtTime > 500) {
                hurtTime = Time.getTimeMillis();
                health -= 15;
                PlatformerAudio.pickup.play();
                int x = MathUtils.random(-10, 10);
                ((PlatformerPlayer) a).launch(x, -10);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // draw monstrosity
        for (int i = 0; i < PIECES; i++) {
            int next = (i + 1) % PIECES;
            Vector2 nextVertex = center.cpy().add(bodyLocal[next]);
            Vector2 vertex = center.cpy().add(bodyLocal[i]);
            shapes.setColor(colors[i]);
            shapes.filledTriangle(center, vertex, nextVertex);
        }

        // draw health
        shapes.setColor(Color.SALMON);
        shapes.filledRectangle(getX(), getY() + 1, 2, 0.2f);
        shapes.setColor(Color.LIME);
        float perc = health / 100f * 2f;
        shapes.filledRectangle(getX(), getY() + 1, perc, 0.2f);

        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        center.set(getX(), getY());
        for (int i = 0; i < PIECES; i++) {
            float r = Math.abs(MathUtils.sin(Time.getTime() + i)) * 2;
            float g = Math.abs(MathUtils.cos(Time.getTime() + i)) * 2;
            float b = Math.abs(MathUtils.sin(Time.getTime() + i * 2)) * 2;
            colors[i].set(r, g, b, 1f);
        }

        // make spiky circle thiny
        float deltaAngle = 360f / PIECES;
        for (int i = 0; i < PIECES; i++) {
            float offset = MathUtils.sin((Time.getTime() + i) * (mode + 1) * 3) * RADIUS;
            float angle = deltaAngle * i;
            bodyLocal[i].x = MathUtils.cosDeg(angle) * offset;
            bodyLocal[i].y = MathUtils.sinDeg(angle) * offset;
        }

        // if it goes wrong reset
        if (getX() < -10 || getX() > 50 || getY() < -10 || getY() > 50) {
            setPosition(spawn.x, spawn.y);
        }

        // set target
        if (nextMove < 0f) {
            nextMove = MathUtils.random(6, 8);
            target = getRandomPos();
            float dur = MathUtils.random(3, 4);
            tweenX.init(getX(), target.x, dur);
            tweenY.init(getY(), target.y, dur);
        }
        if (tweenX.isBusy()) {
            setX(tweenX.getValue());
        }
        if (tweenY.isBusy()) {
            setY(tweenY.getValue());
        }

        // modes
        if (modeTimer < 0f) {
            mode = (mode + 1) % 3;
            switch (mode) {
                case 0:
                    modeTimer = 5f;
                    break;
                case 1:
                    modeTimer = 3f;
                    break;
                case 2:
                    modeTimer = 1f;
                    break;
            }
        }
        switch (mode) {
            case 0: // idle
                break;
            case 1: // slow fire
                if (shootTimer < 0f) {
                    shootTimer = MathUtils.random(0.8f, 1.3f);
                    getStage().addActor(new TurretProjectile(getX(), getY(), Direction.None));
                }
                break;
            case 2: // rapid fire
                if (shootRapidTimer < 0f) {
                    shootRapidTimer = MathUtils.random(0.2f, 0.4f);
                    getStage().addActor(new TurretProjectile(getX(), getY(), Direction.None));
                }
                break;
        }

        shootTimer -= delta;
        shootRapidTimer -= delta;
        modeTimer -= delta;
        nextMove -= delta;
        if (!PlatformerPlayer.died) {
            health -= delta;
        }

        // victory
        if (health <= 0) {
            PlatformerAudio.powerup.play();
            PlatformerStage.victory();
            remove();
        }
        if (PlatformerStage.isCheating && printTimer < 0) {
            printTimer = 3;
            float perc = health / 100f * 2f;
            MagmaLogger.log(this, health + " " + perc);
        }
        printTimer -= delta;
        super.act(delta);
    }

    private Vector2 getRandomPos() {
        return new Vector2(MathUtils.random(28), MathUtils.random(14));
    }

}
