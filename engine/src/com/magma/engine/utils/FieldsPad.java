package com.magma.engine.utils;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public abstract class FieldsPad {
    public FieldsPad() {

    }

    public <T> void setValue(String name, T value) throws ReflectionException {
        Field field;
        try {
            field = ClassReflection.getField(getClass(), name);
            field.set(this, value);
        } catch (ReflectionException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String name) throws ReflectionException {
        Field field;
        try {
            field = ClassReflection.getField(getClass(), name);
            T value = (T) field.get(this); // On exception: Is field of different type?
            return value;
        } catch (ReflectionException e) {
            throw e;
        }
    }
}
