package io.shaka.some;

import java.util.Random;

public interface SomeBuilder<T> {
    static Random  random = new Random();
    public T build();

}
