package com.timgroup.blankapp;

import com.timgroup.structuredevents.Slf4jEventSink;
import com.timgroup.structuredevents.standardevents.ApplicationStarted;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.TimeZone;

import static java.lang.Runtime.getRuntime;
import static java.util.stream.Collectors.toMap;

public class Launcher {
    public static Config loadConfig(String path) {
        return ConfigFactory.parseFile(new File(path), ConfigParseOptions.defaults().setSyntax(ConfigSyntax.PROPERTIES));
    }

    private static void setUpTimezone() {
        System.setProperty("user.timezone", "GMT");
        TimeZone.setDefault(null);
    }

    private static void setUpLogging(Config config) {
        System.setProperty("log.directory", config.getString("log.directory"));
        System.setProperty("log.tags", config.getString("log.tags"));
        System.setProperty("timgroup.app.version", Launcher.class.getPackage().getImplementationVersion() != null
                ? Launcher.class.getPackage().getImplementationVersion() : "");
    }

    private static void setUpMetrics(Config config) {
        Metrics metrics = new Metrics(metricsConfig(config));
        metrics.addJvmMetrics();
        metrics.start();
        Runtime.getRuntime().addShutdownHook(new Thread("metrics shutdownHook") {
            @Override public void run() { metrics.stop(); }
        });
    }

    private static MetricsConfig metricsConfig(final Config config) {
        return new MetricsConfig() {
            @Override public boolean enabled() { return config.getBoolean("graphite.enabled"); }
            @Override public String host() { return config.getString("graphite.host"); }
            @Override public int port() { return config.getInt("graphite.port"); }
            @Override public String prefix() { return config.getString("graphite.prefix"); }
        };
    }

    public static void main(String[] args) {
        setUpTimezone();
        Config config = loadConfig(args[0]);
        setUpLogging(config);
        setUpMetrics(config);
        App app = new App(config);

        app.start();

        getRuntime().addShutdownHook(new Thread() {
            @Override public void run() {
                app.stop();
            }
        });

        LoggerFactory.getLogger(Launcher.class).info("Started file-feed-cache");
        new Slf4jEventSink(LoggerFactory.getLogger(Launcher.class)).sendEvent(
                ApplicationStarted.withVersionAndParameters(System.getProperty("timgroup.app.version"), config.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().unwrapped()))).filtered()
        );
    }
}
