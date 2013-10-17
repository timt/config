package io.shaka.config;

import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static io.shaka.config.Config.APPLICATION_PROPERTIES;
import static io.shaka.config.Config.FALLBACK_PROPERTIES;
import static io.shaka.config.Config.load;
import static io.shaka.some.SomeValues.*;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConfigTest {
    String key = someKey();
    String value = someValue();

    @Test
    public void willLoadSystemProperties() {
        System.setProperty(key, value);
        assertThat(load().get(key), is(value));
    }

    private String someValue() {
        return some(string);
    }

    @Test(expected = ConfigValueNotFoundForKeyException.class)
    public void throwsSensibleExceptionWhenPropetyDoesNotExist() {
        load().get(someValue());
    }

    @Test
    public void willgetStringValue() {
        System.setProperty(key, value);
        assertThat(load().getString(key), is(value));
    }

    @Test
    public void willgetIntValue() {
        int value = some(integer);
        System.setProperty(key, Integer.toString(value));
        assertThat(load().getInt(key), is(value));
    }

    @Test
    public void readsPropertiesFromApplicationPropertiesFile() {
        addProperty(key, value, APPLICATION_PROPERTIES);
        assertThat(load().get(key), is(value));
    }

    @Test
    public void applicationPropertiesDoNotOverrideSystemProperties() {
        String fallbackValue = some(string);
        System.setProperty(key, value);
        addProperty(key, fallbackValue, APPLICATION_PROPERTIES);
        assertThat(load().get(key), is(value));
    }

    @Test
    public void readsPropertiesFromFallbackPropertiesFile() {
        addProperty(key, value, FALLBACK_PROPERTIES);
        assertThat(load().get(key), is(value));
    }

    @Test
    public void fallBackPropertiesDoNotOverrideSystemProperties() {
        String fallbackValue = some(string);
        System.setProperty(key, value);
        addProperty(key, fallbackValue, FALLBACK_PROPERTIES);
        assertThat(load().get(key), is(value));
    }

    @Test
    public void fallBackPropertiesDoNotOverrideApplicationProperties() {
        String fallbackValue = some(string);
        addProperty(key, value, APPLICATION_PROPERTIES);
        addProperty(key, fallbackValue, FALLBACK_PROPERTIES);
        assertThat(load().get(key), is(value));
    }

    @Test
    public void willMergeValues() {
        System.setProperty("my.name", "Tim");
        addProperty("my.surname", "Tennant", APPLICATION_PROPERTIES);
        addProperty(key, "Hello ${my.name} ${my.surname}, how are you today?", FALLBACK_PROPERTIES);
        assertThat(load().get(key), is("Hello Tim Tennant, how are you today?"));
    }

    @Test
    public void willRecurivelyMergeValues() {
        System.setProperty("my.name", "Tim");
        addProperty("my.fullname", "${my.name} Tennant", APPLICATION_PROPERTIES);
        addProperty(key, "Hello ${my.fullname}, how are you today?", FALLBACK_PROPERTIES);
        assertThat(load().get(key), is("Hello Tim Tennant, how are you today?"));
    }


    private void addProperty(String key, String value, Path propertyFilePath) {
        Properties props = new Properties();
        props.setProperty(key, value);
        try {
            props.store(new FileWriter(propertyFilePath.toFile()), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String someKey() {
        return some(string.ofLength(4));
    }


}
