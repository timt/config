package io.shaka.config;

import org.junit.Test;

import static io.shaka.config.Config.load;
import static io.shaka.some.SomeValues.*;
import static java.io.File.separator;
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

    @Test(expected = ConfigValueNotFoundForKeyException.class)
    public void throwsSensibleExceptionWhenPropetyDoesNotExist() {
        load().get(key);
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
        assertThat(load().get("foo.bar"), is("bar"));
    }

    @Test
    public void applicationPropertiesDoNotOverrideSystemProperties() {
        System.setProperty("foo.bar", "not-bar");
        assertThat(load().get("foo.bar"), is("not-bar"));
        System.clearProperty("foo.bar");
        assertThat(load().get("foo.bar"), is("bar"));
    }

    @Test
    public void readsPropertiesFromFallbackPropertiesFile() {
        assertThat(load().get("fin.barr"), is("saunders"));
    }

    @Test
    public void fallBackPropertiesDoNotOverrideSystemProperties() {
        System.setProperty("fin.barr", "not-saunders");
        assertThat(load().get("fin.barr"), is("not-saunders"));
    }

    @Test
    public void fallBackPropertiesDoNotOverrideApplicationProperties() {
        assertThat(load().get("foo.bar"), is("bar"));
    }

    @Test
    public void willMergeValues() {
        System.setProperty("first.name", "Wade");
        assertThat(load().get("greeting"), is("hello Wade Owen"));
    }

    @Test
    public void willRecurivelyMergeValues() {
        System.setProperty("first.name", "Wade");
        assertThat(load().get("greeting2"), is("I know who Parzival(Wade Owen) really is"));
    }

    @Test
    public void canSpecifyRootPackageForPropertiesFiles() {
        String propertiesPackagePath = "easter" + separator + "egg";
        assertThat(load(propertiesPackagePath).get("aech"), is("Helen Harris"));
    }


    private String someKey() {
        return some(string.ofLength(4));
    }

    private String someValue() {
        return some(string);
    }



}
