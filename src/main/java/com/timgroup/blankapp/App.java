package com.timgroup.blankapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.timgroup.structuredevents.EventSink;
import com.timgroup.structuredevents.heartbeat.LoggingHeartbeatScheduler;
import com.timgroup.structuredevents.standardevents.ApplicationStarted;
import com.timgroup.tucker.info.Component;
import com.timgroup.tucker.info.async.AsyncComponent;
import com.timgroup.tucker.info.async.AsyncComponentScheduler;
import com.timgroup.tucker.info.component.JvmVersionComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;

public class App {
    public static final String AppName = "blank-java-worker-app";
    private final JettyService jettyService;
    private final LoggingHeartbeatScheduler loggingHeartbeatScheduler;
    private final EventSink eventSink;
    private final Map<String, Object> configParameters;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AsyncComponentScheduler statusComponentScheduler;

    public App(Properties config, EventSink eventSink) {
        int port = Optional.ofNullable(config.getProperty("port")).map(Integer::parseInt).orElseThrow(() -> new IllegalStateException("No 'port' property"));
        List<Component> statusComponents = singletonList(new JvmVersionComponent());
        this.statusComponentScheduler = AsyncComponentScheduler.createFromAsync(asyncComponentsIn(statusComponents));
        this.jettyService = new JettyService(AppName,
                                             port,
                                             statusComponents);
        this.configParameters = config.entrySet().stream().collect(toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));
        this.eventSink = eventSink;
        this.loggingHeartbeatScheduler = new LoggingHeartbeatScheduler(eventSink);
    }

    public int port() {
        return jettyService.port();
    }

    public void start() {
        loggingHeartbeatScheduler.start();
        statusComponentScheduler.start();
        jettyService.start();

        log.info("Started {}", AppName);
        eventSink.sendEvent(ApplicationStarted.withVersionAndParameters(System.getProperty("timgroup.app.version"),
                configParameters).filtered());
    }

    public void stop() {
        log.info("Stopping {}", AppName);

        jettyService.stop();
        try {
            statusComponentScheduler.stop();
        } catch (InterruptedException e) {
            log.warn("Ignoring exception stopping status component scheduler: " + e);
        }
        loggingHeartbeatScheduler.stop();
    }

    private static List<AsyncComponent> asyncComponentsIn(Collection<? extends Component> components) {
        List<AsyncComponent> output = new ArrayList<>(components.size());
        components.forEach(c -> {
            if (c instanceof AsyncComponent) {
                output.add((AsyncComponent) c);
            }
        });
        return output;
    }
}
