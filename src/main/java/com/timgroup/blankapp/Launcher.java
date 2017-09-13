package com.timgroup.blankapp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;

import com.timgroup.logger.FilebeatAppender;
import com.timgroup.structuredevents.Slf4jEventSink;

import static java.lang.Runtime.getRuntime;

public class Launcher {
    public static Properties loadConfig(String path) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + path, e);
        }
    }

    private static void setUpTimezone() {
        System.setProperty("user.timezone", "GMT");
        TimeZone.setDefault(null);
    }

    private static void setUpLogging(Properties config) {
        FilebeatAppender.configureLoggingProperties(config, Launcher.class);
        System.setProperty("timgroup.app.version", Optional.ofNullable(Launcher.class.getPackage().getImplementationVersion()).orElse(""));
    }

    private static void setUpMetrics(Properties config) {
        Metrics metrics = new Metrics(metricsConfig(config));
        metrics.addJvmMetrics();
        metrics.start();
        Runtime.getRuntime().addShutdownHook(new Thread(metrics::stop, "metrics-shutdown"));
    }

    private static MetricsConfig metricsConfig(final Properties config) {
        return new MetricsConfig() {
            @Override public boolean enabled() { return Optional.ofNullable(config.getProperty("graphite.enabled")).map(Boolean::parseBoolean).orElse(false); }
            @Override public String host() { return config.getProperty("graphite.host"); }
            @Override public int port() { return Optional.ofNullable(config.getProperty("graphite.port")).map(Integer::parseInt).orElse(0); }
            @Override public String prefix() { return config.getProperty("graphite.prefix"); }
        };
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Syntax: java " + Launcher.class.getName() + " config.properties");
            System.exit(1);
        }

        setUpTimezone();
        Properties config = loadConfig(args[0]);
        setUpLogging(config);
        setUpMetrics(config);
        App app = new App(config, new Slf4jEventSink());

        app.start();

        getRuntime().addShutdownHook(new Thread(app::stop, "shutdown"));
    }
}
