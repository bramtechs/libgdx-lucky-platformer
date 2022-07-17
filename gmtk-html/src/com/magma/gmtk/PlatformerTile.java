package com.magma.gmtk;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.magma.engine.assets.Shapes;
import com.magma.engine.collision.TriggerListener;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlatformerTile extends Actor implements TriggerListener {

    protected ShapeDrawer shapes;
    protected PlatformerStage stage;

    public PlatformerTile(int x, int y) {
        shapes = Shapes.getInstance();
        setPosition(x, y);
        setSize(1f, 1f);
    }

    @Override
    public void act(float delta) {
        if (stage == null){
            stage = (PlatformerStage) getStage();
        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setRectColor();
        drawRect();
        super.draw(batch, parentAlpha);
    }

    public void onHitByPlayer(PlatformerPlayer player){
    }

    public void setRectColor(){
        shapes.setColor(PlatformerStage.activePalette.getFgColor());
    }

    public void drawRect(){
        shapes.filledRectangle(getX(),getY(),getWidth(),getHeight());
    }

	@Override
	public void onTriggered(Actor a) {
        if (a instanceof PlatformerPlayer){
            PlatformerPlayer player = (PlatformerPlayer) a;
            onHitByPlayer(player);
        }
	}
}
