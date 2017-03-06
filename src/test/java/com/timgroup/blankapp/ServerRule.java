package com.timgroup.blankapp;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.timgroup.structuredevents.Event;
import com.timgroup.structuredevents.LocalEventSink;
import org.junit.rules.ExternalResource;

public class ServerRule extends ExternalResource {
    private App app;
    private final LocalEventSink eventSink = new LocalEventSink();

    @Override
    protected void before() throws Throwable {
        Properties properties = new Properties();
        overrideConfigProperties().forEach((k, v) -> {
            properties.setProperty(k, String.valueOf(v));
        });
        app = new App(properties, eventSink);
        app.start();
    }

    @Override
    protected void after() {
        try {
            app.stop();
        } catch (Exception e) { /* */ }
    }

    public Map<String, Object> overrideConfigProperties() {
        return Collections.singletonMap("port", 0);
    }

    public List<Event> events() {
        return eventSink.events();
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
}
