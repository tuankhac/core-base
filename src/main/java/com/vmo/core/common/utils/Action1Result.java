package com.vmo.core.common.utils;

public interface Action1Result<T1, T2> {
    T2 call(T1 t);
}
