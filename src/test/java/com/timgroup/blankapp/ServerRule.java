package com.timgroup.blankapp;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.timgroup.structuredevents.Event;
import com.timgroup.structuredevents.EventSink;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.rules.ExternalResource;

import static com.timgroup.blankapp.Launcher.loadConfig;

public class ServerRule extends ExternalResource {
    private App app;
    private final LocalEventSink eventSink = new LocalEventSink();

    @Override
    protected void before() throws Throwable {
        Config fromConfigFile = loadConfig("config.properties");
        Config config = ConfigFactory.parseMap(overrideConfigProperties()).resolveWith(fromConfigFile);
        app = new App(config, eventSink);
        app.start();
    }

    @Override
    protected void after() {
        try {
            app.stop();
        } catch (Exception e) {
        }
    }

    public Map<String, Object> overrideConfigProperties() {
        return Collections.singletonMap("port", 0);
    }

    public List<Event> events() {
        return eventSink.events;
    }

    public App app() {
        if (app == null) {
            throw new IllegalStateException("app not running");
        }
        return app;
    }

    public URI uri(String path) {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must start with '/'");
        }
        return URI.create(String.format("http://localhost:%d%s", app().port(), path));
    }

    private static final class LocalEventSink implements EventSink {
        private final List<Event> events = new CopyOnWriteArrayList<>();

        @Override
        public void sendEvent(Event event) {
            events.add(event);
        }

        @Override
        public void sendWarning(Event event) {
            events.add(event);
        }
    }
}
