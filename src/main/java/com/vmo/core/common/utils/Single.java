package com.vmo.core.common.utils;

public class Single<T> {
    private T value;

    public Single(T value) {
        this.value = value;
    }

    public Single() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
