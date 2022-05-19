package com.vmo.core.common.utils;

public class Third<T1, T2, T3> extends Pair<T1, T2> {
    private T3 third;

    public Third(T1 first, T2 second, T3 third) {
        super(first, second);
        this.third = third;
    }

    public Third() {

    }

    public T3 getThird() {
        return third;
    }

    public void setThird(T3 third) {
        this.third = third;
    }
}
