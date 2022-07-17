package com.magma.engine.utils;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.magma.engine.maps.triggers.CustomTrigger;

public class MagmaMath {
	public static Rectangle scaleRectangle(Rectangle rect, float scale) {
		rect.x *= scale;
		rect.y *= scale;
		rect.width *= scale;
		rect.height *= scale;
		return rect;
	}

	public static enum PadAlign {
		LEFT, CENTER, RIGHT
	}

	public static String padString(String input, char padSymbol, int totalLength, PadAlign align) {
		// check if text to short
		if (input.length() >= totalLength) {
			return input;
		}
		
		String result;
		switch (align) {
		case CENTER:
			throw new IllegalArgumentException("Alignment not implemented");
		case RIGHT:
			result = input;
			for (int i = 0; i < totalLength - input.length(); i++) {
				result += padSymbol;
			}
			break;
		case LEFT:
			result = "";
			for (int i = 0; i < totalLength - input.length(); i++) {
				result += padSymbol;
			}
			result += input;
			break;
		default:
			throw new IllegalArgumentException("Alignment not implemented");
		}
		return result;
	}
	

	public static float getDistanceToCircle(float x, float y, Circle circle) {
		return (circle.x - x) * (circle.x - x) + (circle.y - y) * (circle.y - y);
	}
	
	public static boolean shapesOverlap(Shape2D a, Shape2D b) { // this is probably dumb
		if (a instanceof Circle) {
			Circle circle = (Circle) a;
			if (b instanceof Circle) {
				return circle.overlaps((Circle) b);
			}
			if (b instanceof Rectangle) {
				return Intersector.overlaps(circle,(Rectangle) b);
			}
		}
		else if (a instanceof Rectangle) {
			Rectangle rect = (Rectangle) a;
			if (b instanceof Rectangle) {
				return Intersector.overlaps(rect, (Rectangle) b);
			}
			if (b instanceof Circle) {
				return Intersector.overlaps((Circle) b, rect);
				//return rectOverlapsCircle(rect, (Circle) b);
			}
		}
		throw new IllegalArgumentException("Invalid shapes");
	}

	public static Shape2D extractShape(Actor actor) {
		Shape2D shape;
		if (actor instanceof CustomTrigger) {
			shape = ((CustomTrigger) actor).getShape();
		} else {
			shape = new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight()); // TODO pool?
		}
		return shape;
	}
}
