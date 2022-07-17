package com.magma.engine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class Palette {
    private static Array<Palette> palettes;

    static {
        palettes = new Array<Palette>();

        // add default palette
        Color bg = new Color(59 / 255f, 85 / 255f, 112 / 255f, 1f);
        Color fg = new Color(144 / 255f, 182 / 255f, 222 / 255f, 1f);
        Palette defaultPalette = new Palette(bg, fg);
        registerPalette(defaultPalette);
    }

    public static int latestID;

    /**
     * Adds a Palette instance to the engine. Palettes can be called back with
     * getPalette(int id)
     * MagmaEngine has a default palette with index 0.
     * 
     * @param palette should be instantiated in static fields
     * @return the palette with the id that got registered
     */
    public static int registerPalette(Palette palette) {
        palettes.add(palette);
        return palette.getID();
    }

    public static Palette getPalette(int id) {
        if (id >= Palette.latestID) {
            throw new IllegalArgumentException("No palette registered with id " + id);
        }
        return palettes.get(id);
    }

    public static Palette getDefault() {
        return palettes.first();
    }

    private final int id;
    private final Color bgColor;
    private final Color fgColor;

    public Palette(Color bgColor, Color fgColor) {
        this.id = latestID;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        latestID++;
    }

    public int getID() {
        return id;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public Color getFgColor() {
        return fgColor;
    }

}
