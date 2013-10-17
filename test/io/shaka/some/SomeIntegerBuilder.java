package io.shaka.some;

public class SomeIntegerBuilder implements SomeBuilder<Integer> {
    protected static SomeIntegerBuilder integer = new SomeIntegerBuilder();


    private SomeIntegerBuilder() {
    }

    public Integer build() {
        return random.nextInt();
    }

    // some(integer.from(3).to(5))

}