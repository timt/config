package io.shaka.config;

class ConfigValueNotFoundForKeyException extends RuntimeException{
    protected ConfigValueNotFoundForKeyException(String key) {
        super(key);
    }
}
