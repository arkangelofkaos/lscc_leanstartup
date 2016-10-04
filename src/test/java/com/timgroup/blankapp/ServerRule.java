package com.timgroup.blankapp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.timgroup.structuredevents.Event;
import com.timgroup.structuredevents.EventSink;
import org.junit.rules.ExternalResource;

import static com.timgroup.blankapp.Launcher.loadConfig;

public class ServerRule extends ExternalResource {
    private App app;
    private final LocalEventSink eventSink = new LocalEventSink();

    @Override
    protected void before() throws Throwable {
        app = new App(loadConfig("config.properties"), eventSink);
        app.start();
    }

    @Override
    protected void after() {
        try {
            app.stop();
        } catch (Exception e) {
        }
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
