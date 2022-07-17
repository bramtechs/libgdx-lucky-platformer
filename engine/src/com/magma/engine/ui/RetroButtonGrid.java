package com.magma.engine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class RetroButtonGrid extends Table {

    private final String[] options;
    private final RetroButton[][] buttons;
    private final int columns;
    private final int rows;
    private final GridPoint2 selected;

    private boolean hasFocus;

    public RetroButtonGrid(String... options) {
        this.options = options;
        this.columns = (int) Math.max(2, Math.sqrt(options.length));
        this.rows = options.length / columns;
        this.buttons = new RetroButton[columns][rows];
        this.selected = new GridPoint2(0, 0);

        for (int i = 0; i < options.length; i++) {
            final int id = i; // java lol
            String option = options[i];
            RetroButton butt = new RetroButton(option);
            if (i == 0) {
                butt.select();
            } else if (i % columns == 0) {
                row();
            }
            add(butt).expand();
            int x = i % columns;
            int y = i / columns;
            buttons[x][y] = butt;
        }
    }

    public void focus() {
        unfocus(); // clear previous user input
        hasFocus = true;
        // select the first button
        setCursor(0, 0);
    }

    public void unfocus() {
        hasFocus = false;
        deselectAll();
    }

    private void deselectAll() {
        // deselect all buttons
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                buttons[x][y].deselect();
            }
        }
    }

    public RetroButton getSelected() {
        return buttons[selected.x][selected.y];
    }

    private int getSelectedID() {
        return selected.y * columns + selected.x;
    }

    public void moveCursor(int x, int y) {
        setCursor(selected.x + x, selected.y + y);
    }

    public void setCursor(int x, int y) {
        selected.x = MathUtils.clamp(x, 0, columns - 1);
        selected.y = MathUtils.clamp(y, 0, rows - 1);
        deselectAll();
        buttons[selected.x][selected.y].select();
    }

    protected abstract void pressed(int id);

    @Override
    public void act(float delta) {
        if (hasFocus) {
            // TODO: use keymapping
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                int id = getSelectedID();
                pressed(id);
            }
            if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
                moveCursor(-1, 0);
            }
            if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
                moveCursor(1, 0);
            }
            if (Gdx.input.isKeyJustPressed(Keys.UP)) {
                moveCursor(0, -1);
            }
            if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
                moveCursor(0, 1);
            }
        }
        super.act(delta);
    }

}
