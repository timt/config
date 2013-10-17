package io.shaka.some;

public class SomeStringBuilder implements SomeBuilder<String> {
    private final int length;
    protected static SomeStringBuilder string = new SomeStringBuilder(10);


    private SomeStringBuilder(int length) {
        this.length = length;
    }

    public SomeStringBuilder ofLength(int length) {
        return new SomeStringBuilder(length);
    }


    public String build() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }



}
