package com.magma.engine.ui;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.magma.engine.assets.MagmaLoader;

public class RetroHealthBar extends ProgressBar {

    private final Palette colors;
    private final Skin skin;
    private final BitmapFont font;
    private final GridPoint2 size;
    private Label prefixLabel;

    public RetroHealthBar(GridPoint2 size, float min, float max, Palette palette) {
        super(min, max, 0.01f, false, new ProgressBarStyle());
        this.skin = MagmaLoader.getDebugSkin();
        this.size = size;
        this.colors = palette;
        this.font = skin.getFont("default-font"); //TODO: fix hacky solution
        applyGraphics();
    }

    private void applyGraphics() {
        final int WIDTH = size.x;
        final int HEIGHT = size.y;
        getStyle().background = MagmaGfx.getColoredDrawable(WIDTH, HEIGHT, colors.getBgColor());
        getStyle().knob = MagmaGfx.getColoredDrawable(WIDTH, HEIGHT, colors.getFgColor());
        getStyle().knobBefore = MagmaGfx.getColoredDrawable(WIDTH, HEIGHT, colors.getFgColor());

        setAnimateDuration(0.0f);
        setValue(1f);
        setAnimateDuration(0.25f);
    }

    public void addIntoTable(Table table,String name){
        prefixLabel = new Label(name,skin);
        prefixLabel.setVisible(this.isVisible());
        table.add(prefixLabel).left();
        table.add(this).width(size.x).height(size.y);
        table.row();
    }

    private String getText() {
        return (int)getValue() + "/" + (int)getMaxValue();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch,getText(),getX()+3,getY()+getHeight()*0.85f);
    }

    @Override
    public void setVisible(boolean visible) {
        if (prefixLabel != null){
            prefixLabel.setVisible(visible);
        }
        super.setVisible(visible);
    }

}
