package io.shaka.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    protected static String FALLBACK_PROPERTIES = "fallback.properties";
    protected static String APPLICATION_PROPERTIES = "application.properties";
    private final ConfigMap values;
    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\[.*?\\]|\\$\\{.*?\\}");

    private Config(Path searchPath) {
        this.values = new ConfigMap(searchPath);
    }

    public static Config    load(String packagePath) {
        return new Config(Paths.get(packagePath));
    }

    public static Config load() {
        return new Config(Paths.get(""));
    }

    public String get(String key) {
        return values.get(key);
    }

    public String getString(String key) {
        return values.get(key);
    }

    public Integer getInt(String key) {
        return Integer.valueOf(get(key));
    }


    private static class ConfigMap {
        private final Map<Object, Object> values = System.getProperties();
        private final Path searchPath;

        private ConfigMap(Path searchPath) {
            this.searchPath = searchPath;
            addPropertiesFor(APPLICATION_PROPERTIES);
            addPropertiesFor(FALLBACK_PROPERTIES);
        }

        private void addPropertiesFor(String propertiesFile) {
            InputStream resource = getResourceAsStream(propertiesFile);
            try {
                if (resource != null) {
                    Properties props = new Properties();
                    props.load(resource);
                    for (Map.Entry<Object, Object> entry : props.entrySet()) {
                        if (!values.containsKey(entry.getKey()))
                            values.put(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private InputStream getResourceAsStream(String propertiesFile) {
            String propertiesFilePath = searchPath.resolve(propertiesFile).toString();
            return getClass().getClassLoader().getResourceAsStream(propertiesFilePath);
        }

        private String get(String key) {
            if (values.containsKey(key)) return resolveTemplates(values.get(key).toString());
            throw new ConfigValueNotFoundForKeyException(key);
        }

        private String resolveTemplates(String value) {
            Matcher matcher = TEMPLATE_PATTERN.matcher(value);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                String replacement = get(stripTemplateBraces(matcher.group(0)));
                if (replacement != null) {
                    matcher.appendReplacement(buffer, "");
                    buffer.append(replacement);
                }
            }
            matcher.appendTail(buffer);
            return buffer.toString();
        }

        private String stripTemplateBraces(String template) {
            return template.substring(2, template.length() - 1);
        }

    }

}
