package com.magma.gmtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.magma.engine.assets.Shapes;
import com.magma.engine.chars.CharacterController.Direction;
import com.magma.engine.collision.TriggerListener;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class TurretProjectile extends Actor implements TriggerListener {

    private ShapeDrawer shapes;
    private Direction dir;

    private Vector2 aim;

    public TurretProjectile(float x, float y, Direction dir) {
        this.dir = dir;
        shapes = Shapes.getInstance();
        setPosition(x+0.30f,y+0.30f);
        setSize(0.4f,0.4f);

        // if direction none aim at the player
        if (dir == Direction.None){
            float dx = PlatformerPlayer.instance.getX()-x;
            float dy = PlatformerPlayer.instance.getY()-y;
            aim = new Vector2(dx,dy).nor();
        }
    }

    @Override
    public void onTriggered(Actor a) {
        if (a instanceof PlatformerPlayer) {
            PlatformerPlayer.explode();
        }
    }

    @Override
    public void act(float delta) {
        float speed = delta*10;
        switch (dir){
			case Down:
                moveBy(0, -speed);
				break;
			case Left:
                moveBy(-speed, 0);
				break;
			case Right:
                moveBy(speed, 0);
				break;
			case Up:
                moveBy(0, speed);
				break;
			default:
				break;
        }
        if (getX() < -10 || getX() > 400 || getY() < -10 || getY() > 400){
            remove();
        }

        if (dir == Direction.None && aim != null){
            moveBy(aim.x*speed, aim.y*speed);
        }

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapes.setColor(Color.RED);
        shapes.filledRectangle(getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

}
