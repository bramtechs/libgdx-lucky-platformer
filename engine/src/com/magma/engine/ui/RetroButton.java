package com.magma.engine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.assets.Shapes;
import com.magma.engine.utils.Time;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class RetroButton extends Label {

    private final static float OFFSET_X = -50;
    private final static float SIZE = 20;
    private final static float ANIM_D = 5;
	private final static float ANIM_SPEED = 5;

    private final Vector2 vertexTop;
    private final Vector2 vertexBot;
    private final Vector2 vertexRight;

    private final ShapeDrawer shapes;

    private float selectTime;
    protected boolean isSelected;

    public RetroButton(String text) {
        super(text, MagmaLoader.getDebugSkin());
        this.shapes = Shapes.getInstance();
        this.vertexBot = new Vector2();
        this.vertexTop = new Vector2();
        this.vertexRight = new Vector2();
    }

    public void select() {
        isSelected = true;
        selectTime = Time.getTime();
    }

    public void deselect() {
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // determine the shape of the arrow
        // TODO: do not calculate every frame
        float centerX = getX() + getWidth() * 0.5f;
        float centerY = getY() + getHeight() * 0.5f;
        float offset = OFFSET_X + 10 * 12; // TODO: fix
        // animate that boi
        float dtime = Time.getTime()-selectTime;
        offset += Math.sin(dtime*ANIM_SPEED) * ANIM_D;

        this.vertexTop.set(centerX - offset - SIZE, centerY + SIZE * 0.5f);
        this.vertexBot.set(centerX - offset - SIZE, centerY - SIZE * 0.5f);
        this.vertexRight.set(centerX - offset, centerY);

        if (isSelected) {
            shapes.setColor(getColor());
            shapes.filledTriangle(vertexTop, vertexRight, vertexBot);
        }
        super.draw(batch, parentAlpha);
    }

}
