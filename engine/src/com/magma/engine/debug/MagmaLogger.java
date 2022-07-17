package com.magma.engine.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.badlogic.gdx.Gdx;
import com.magma.engine.utils.Time;

public class MagmaLogger {
    protected static MagmaLogger instance;

    protected String name;
    protected String html;

    public static enum Level {
        DEBUG,
        INFO,
        WARN,
        ERROR,
    }

    public MagmaLogger(String gameName) {
        this.name = gameName;
    }

    public static void logl(Object sender, Object... messages) {
        String combo = "";
        int i = 0;
        for (Object o : messages) {
            String s = o.toString();
            if (i > 0) {
                combo += ", ";
            }
            combo += s;
            i++;
        }
        log(sender, combo, Level.INFO);
    }

    public static void log(Object sender, Object message, Level level) {
        String name;
        if (sender instanceof String) {
            name = sender.toString();
        } else {
            name = sender.getClass().getSimpleName();
        }

        // TODO: messes up after a long time
        String time = Float.toString(Time.getTime());
        name = "(" + time + "s) " + name;

        String mes = message.toString();
        MagmaLogger i = get();
        switch (level) {
            case DEBUG:
                i.html += name + " " + mes + "<br/>";
                break;
            case ERROR:
                i.html += "<strong style='color: red'>" + name + "<br/>" + mes + "</strong><br/>";
                break;
            case INFO:
                i.html += "<strong>" + name + "</strong> " + mes + "<br/>";
                break;
            case WARN:
                i.html += "<strong style='color: yellow'>" + name + "</strong> " + mes + "<br/>";
                break;
            default:
                throw new IllegalArgumentException("Invalid log level");
        }
        Gdx.app.log(name, message.toString());
    }

    public static void log(Object sender, Object message) {
        log(sender, message, Level.INFO);
    }

    public static void log(Object message) {
        log("CODE", message, Level.DEBUG);
    }

    public static <T> void printArray(Object sender, T[] array) {
        String text = " collection: " + sender.getClass().getSimpleName() + " ";
        if (array.length == 0) {
            text += "(empty)";
        } else {
            for (T item : array) {
                text += ", " + item.toString();
            }
        }
        log(sender, text);
    }

    public static void logException(Exception e) {
        get()._logException(e);
    }

    protected void _logException(Exception e){
        e.printStackTrace();
        String message = e.toString();
        log("EXCEPTION", message, Level.ERROR);
    }

    public static void printArray(Object sender, Iterable<?> array) {
        String text = " collection: " + sender.getClass().getSimpleName() + " ";
        int i = 0;
        for (Object item : array) {
            text += ", " + item.toString();
            i++;
        }
        if (i == 0) {
            text += "(empty)";
        }
        log(sender, text);
    }

    public static void init(MagmaLogger instance) {
        if (MagmaLogger.instance != null) {
            throw new IllegalStateException("There is already a logger defined!");
        }
        MagmaLogger.instance = instance;
    }

    public static String getHtmlLog() {
        return get().html;
    }

    private static MagmaLogger get() {
        if (instance == null) {
            throw new NullPointerException("No logger initialized!");
        }
        return instance;
    }
}
