package com.magma.gmtk;

public class SupportTile extends PlatformerTile {

    private boolean isActive;

    public SupportTile(int x, int y) {
        super(x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void activate() {
        isActive = true;
    }

    @Override
    public void setRectColor() {
        if (!isActive) {
            shapes.setColor(PlatformerStage.activePalette.getBgColor().cpy().add(0.4f, 0.4f, 0.4f, 0f));
            return;
        }
        super.setRectColor();
    }

    @Override
    public void drawRect() {
        super.drawRect();
        shapes.setColor(PlatformerStage.activePalette.getBgColor());
        if (!isActive) {
            shapes.line(getX(), getY(), getX() + 1, getY() + 1, 0.1f);
            shapes.line(getX() + 1, getY(), getX(), getY() + 1, 0.1f);
        }
    }

}
