package com.magma.engine.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.magma.engine.maps.MapSession;
import com.magma.engine.maps.triggers.CustomTrigger;

public class SpriteActor extends Actor implements Drawable, CustomTrigger {

	public static boolean autoScale = true;
	
	private TextureRegion region;
	private boolean flipX;
	private boolean flipY;
	
	private float scaleX;
	private float scaleY;
	
	private Rectangle bounds; // cache to avoid GC abuse
	
	public SpriteActor(TextureRegion region) {
		setRegion(region);
		setOrigin(Align.center);
		
		// resize to world units
		if (autoScale) {
			setScale(1f/MapSession.getTileSize().x,1f/MapSession.getTileSize().y);
		}else {
			setScale(1f);
		}
		bounds = new Rectangle();
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
		float w = region.getRegionWidth()*getScaleX();
		float h = region.getRegionHeight()*getScaleY();
		setSize(w,h);
		setOrigin(Align.center);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		/*
		 * region x y originX originY width height scaleX scaleY rotation
		 */
		bounds.x = getX()-getWidth()*0.5f;
		bounds.y = getY()-getHeight()*0.5f;
		bounds.width = getWidth();
		bounds.height = getHeight();
		
		if (isVisible() && getAlpha() > 0.01f) {
			batch.setColor(getColor());
			batch.draw(region, bounds.x, bounds.y, getOriginX(), getOriginY(), bounds.width, bounds.height,
					flipX ? -1f : 1f, flipY ? -1f : 1f, getRotation());
			batch.setColor(Color.WHITE);
		}
		//Gdx.app.log("Player", ""+getX() + " x " + getY());
		
		super.draw(batch, parentAlpha);
	}
	
	/**
	 * Gives the position and dimensions of this SpriteActor in an Rectangle 
	 * @return Rectangle is a reference! Do not edit!
	 */
	public Rectangle getRegion() {
		return bounds;
	}
	
	public float getCenterX() {
		return bounds.x+bounds.width*0.5f;
	}
	
	public float getCenterY() {
		return bounds.y+bounds.height*0.5f;
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes) {
		shapes.setColor(Color.CYAN);
		shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        // draw collision shape if it has a custom one
        if (this instanceof CustomTrigger){
            CustomTrigger trig = this;
            Shape2D shape = trig.getShape();
            shapes.setColor(Color.YELLOW);
            if (shape instanceof Rectangle){
                Rectangle rect = (Rectangle)shape;
                shapes.rect(rect.x,rect.y,rect.width,rect.height);
            }else if (shape instanceof Circle){
                Circle circle = (Circle)shape;
                shapes.circle(circle.x,circle.y,circle.radius);
            }
        }

		shapes.setColor(Color.YELLOW);
		shapes.circle(getCenterX(), getCenterY(),0.5f);
	}
	
	// I'M USING MY OWN EASIER SCALING SYSTEM
	@Override
	public void setScale(float scaleX, float scaleY) {
		setScaleX(scaleX);
		setScaleY(scaleY);
	}
	
	public void setAlpha(float alpha) {
		setColor(getColor().r,getColor().g,getColor().b,alpha);
	}
	
	public float getAlpha() {
		return getColor().a;
	}
	
	@Override
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}
	
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	@Override
	public float getScaleX() {
		return scaleX;
	}
	
	@Override
	public float getScaleY() {
		return scaleY;
	}
	
	@Override
	public void setScale(float scaleXY) {
		setScaleX(scaleXY);
		setScaleY(scaleXY);
	}
	
	public void setFlipX(boolean flip) {
		this.flipX = flip;
	}

	public void setFlipY(boolean flip) {
		this.flipY = flip;
	}

	public void setFlip(boolean flipX, boolean flipY) {
		setFlipX(flipX);
		setFlipY(flipY);
	}

    public void setPosition(GridPoint2 position){
        super.setPosition(position.x, position.y);
    }
    public void setPosition(Vector2 position){
        super.setPosition(position.x, position.y);
    }

	public boolean isFlipX() {
		return flipX;
	}

	public boolean isFlipY() {
		return flipY;
	}

	@Override
	public Shape2D getShape() {
		return getRegion();
	}
}
