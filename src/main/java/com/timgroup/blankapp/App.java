package com.timgroup.blankapp;

import java.util.Map;

import com.timgroup.blankapp.monitoring.StatusPage;
import com.timgroup.structuredevents.EventSink;
import com.timgroup.structuredevents.standardevents.ApplicationStarted;
import com.timgroup.tucker.info.component.JvmVersionComponent;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

public class App {
    public static final String AppName = "blank-java-worker-app";
    private final StatusPage statusPage;
    private final EventSink eventSink;
    private final Map<String, Object> configParameters;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public App(Config config, EventSink eventSink) {
        this.statusPage = new StatusPage(AppName,
                                         config.getInt("port"),
                                         asList(new JvmVersionComponent()));
        this.configParameters = config.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().unwrapped()));
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
