package io.shaka.config;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    protected static Path FALLBACK_PROPERTIES = Paths.get("fallback.properties");
    protected static Path APPLICATION_PROPERTIES = Paths.get("application.properties");
    private final ConfigMap values = new ConfigMap();
    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\[.*?\\]|\\$\\{.*?\\}");

    public static Config load() {
        return new Config();
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

        private ConfigMap() {
            addPropertiesFor(APPLICATION_PROPERTIES);
            addPropertiesFor(FALLBACK_PROPERTIES);
        }

        private void addPropertiesFor(Path propertiesFilePath) {
            try {
                Properties fallback = new Properties();
                fallback.load(new FileReader(propertiesFilePath.toFile()));
                for (Map.Entry<Object, Object> entry : fallback.entrySet()) {
                    if (!values.containsKey(entry.getKey()))
                        values.put(entry.getKey(), entry.getValue());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
