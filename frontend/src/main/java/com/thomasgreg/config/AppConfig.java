package com.thomasgreg.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                throw new RuntimeException("Arquivo config.properties não encontrado.");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao carregar config.properties", ex);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
