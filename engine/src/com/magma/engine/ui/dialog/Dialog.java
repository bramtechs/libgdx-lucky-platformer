package com.magma.engine.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.assets.Shapes;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Dialog extends Stack {

    private static Dialog instance;
    private final Queue<DialogMessage> messages;

    protected ShapeDrawer shapes;
    private TextArea text;

    private boolean isLocked;

    public Dialog(Viewport view, int width, int height) {
        Dialog.instance = this;
        this.shapes = Shapes.getInstance();
        this.messages = new Queue<DialogMessage>();
        this.text = new TextArea("_", MagmaLoader.getDebugSkin());
        this.text.setDisabled(true);
        setPosition((view.getWorldWidth() - width) * 0.5f, -view.getWorldHeight() + 10);
        setSize(width, height);
        Gdx.app.log("Dialog",
                "Dialog created at bounds: " + getX() + " , " + getY() + " (" + getWidth() + " x " + getHeight() + ")");
        addActor(text);
    }

    @Override
    public void act(float delta) {
        if (messages.isEmpty()) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            if (!isLocked) {
                messages.removeFirst();
            } else {
                Gdx.app.log("Dialog", "Dialog is locked");
                return;
            }
        }

        toFront();
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (messages.isEmpty()) {
            return;
        }

        DialogMessage message = messages.first();
        if (message.style == DialogStyle.Basic) {
            shapes.setColor(Color.BLACK);
            shapes.filledRectangle(getX(), getY(), getWidth(), getHeight());
            shapes.setColor(Color.WHITE);
            shapes.rectangle(getX(), getY(), getWidth(), getHeight(), 3f);
            text.setText(message.message);
        } else {
            throw new IllegalArgumentException("No dialog style defined!");
        }
        super.draw(batch, parentAlpha);
    }

    public static boolean isSpeaking() {
        return !getInstance().messages.isEmpty();
    }

    public static void speak(DialogMessage message) {
        Gdx.app.log(">", message.message);
        getInstance().messages.addLast(message);
    }

    public static void speak(String message) {
        speak(new DialogMessage(message));
    }

    public static void speak(int key) {
        speak(DialogMessage.PLACEHOLDER);
    }

    public static boolean isLocked() {
        return getInstance().isLocked;
    }

    public static void lock() {
        getInstance().isLocked = true;
    }

    public static void unlock() {
        getInstance().isLocked = false;
    }

    public static Dialog getInstance() {
        if (instance == null) {
            throw new NullPointerException("No UI to show dialog! Create at least one Dialog instance!");
        }
        return instance;
    }
}
