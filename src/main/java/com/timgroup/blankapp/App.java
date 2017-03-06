package com.timgroup.blankapp;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.timgroup.blankapp.monitoring.StatusPage;
import com.timgroup.structuredevents.EventSink;
import com.timgroup.structuredevents.standardevents.ApplicationStarted;
import com.timgroup.tucker.info.component.JvmVersionComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;

public class App {
    public static final String AppName = "blank-java-worker-app";
    private final StatusPage statusPage;
    private final EventSink eventSink;
    private final Map<String, Object> configParameters;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public App(Properties config, EventSink eventSink) {
        int port = Optional.ofNullable(config.getProperty("port")).map(Integer::parseInt).orElseThrow(() -> new IllegalStateException("No 'port' property"));
        this.statusPage = new StatusPage(AppName,
                                         port,
                                         singletonList(new JvmVersionComponent()));
        this.configParameters = config.entrySet().stream().collect(toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));
        this.eventSink = eventSink;
    }

    public int port() {
        return statusPage.port();
    }

    public void start() {
        statusPage.start();
        log.info("Started {}", AppName);

        eventSink.sendEvent(ApplicationStarted.withVersionAndParameters(System.getProperty("timgroup.app.version"),
                configParameters).filtered());
    }

    public void stop() {
        log.info("Stopping {}", AppName);
        statusPage.stop();
    }
}
