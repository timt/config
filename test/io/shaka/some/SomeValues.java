package io.shaka.some;

public class SomeValues {
    public static <T> T some(SomeBuilder<T> builder) {
        return builder.build();
    }

    public static SomeStringBuilder string = SomeStringBuilder.string;
    public static SomeIntegerBuilder integer = SomeIntegerBuilder.integer;
}
