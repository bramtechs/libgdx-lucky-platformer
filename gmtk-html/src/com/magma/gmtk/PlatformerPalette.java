package com.magma.gmtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.magma.engine.ui.Palette;

public class PlatformerPalette extends Palette {

    private Color playerColor;
    private static int latest;

	public PlatformerPalette(Color bgColor, Color fgColor) {
		super(bgColor, fgColor);
        float r = 1f-fgColor.r;
        float g = 1f-fgColor.g;
        float b = 1f-fgColor.b;
        playerColor = new Color(r,g,b,1f);
	}

    public static PlatformerPalette getRandomPalette(){
        int i = latest;
        while (i == latest){
            i = MathUtils.random(3);
        }
        latest = i;
        switch(i){
            case 0:
                return new PlatformerPalette(Color.GOLDENROD,Color.GOLD);
            case 1:
                return new PlatformerPalette(Color.LIGHT_GRAY,Color.WHITE);
            case 2:
                return new PlatformerPalette(Color.FIREBRICK,Color.SALMON);
            case 3:
                return new PlatformerPalette(Color.OLIVE,Color.LIME);
            default:
                throw new IllegalArgumentException("No color defined");
        }
    }

	public Color getPlayerColor() {
		return playerColor;
	}
}

