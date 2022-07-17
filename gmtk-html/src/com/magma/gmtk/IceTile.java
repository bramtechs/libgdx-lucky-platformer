package com.magma.gmtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public class IceTile extends PlatformerTile {

    public IceTile(int x, int y) {
        super(x, y);
    }

    @Override
    public void setRectColor() {
        shapes.setColor(Color.CYAN);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        shapes.setColor(Color.TEAL);
        shapes.line(getX(), getY(), getX()+1, getY()+1,0.1f);
        shapes.line(getX()+0.5f, getY(), getX()+1, getY()+0.5f,0.1f);
        shapes.line(getX(), getY()+0.5f, getX()+0.5f, getY()+1f,0.1f);
    }

}

