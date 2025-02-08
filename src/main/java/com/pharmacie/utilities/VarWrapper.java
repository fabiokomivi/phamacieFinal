package com.pharmacie.utilities;

public class VarWrapper<T> {
    private T value;

    public VarWrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}