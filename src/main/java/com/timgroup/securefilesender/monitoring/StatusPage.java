package com.timgroup.securefilesender.monitoring;

import com.timgroup.tucker.info.Component;
import com.timgroup.tucker.info.async.AsyncComponentScheduler;
import com.timgroup.tucker.info.component.JarVersionComponent;
import com.timgroup.tucker.info.httpserver.ApplicationInformationServer;
import com.timgroup.tucker.info.status.StatusPageGenerator;

import java.io.IOException;
import java.util.List;

import static com.timgroup.tucker.info.Health.ALWAYS_HEALTHY;
import static com.timgroup.tucker.info.Stoppable.ALWAYS_STOPPABLE;
import static com.timgroup.tucker.info.async.AsyncComponentScheduler.createFromSynchronous;
import static com.timgroup.tucker.info.async.AsyncSettings.settings;
import static java.util.concurrent.TimeUnit.SECONDS;

public class StatusPage {
    private final ApplicationInformationServer infoServer;
    private final AsyncComponentScheduler scheduler;

    public StatusPage(String name, int port, List<Component> components) {
        try {
            StatusPageGenerator generator = new StatusPageGenerator(name, new JarVersionComponent(getClass()));
            this.scheduler = createFromSynchronous(components, settings().withRepeatSchedule(10, SECONDS));
            this.infoServer = ApplicationInformationServer.create(port, generator, ALWAYS_STOPPABLE, ALWAYS_HEALTHY);
            scheduler.addComponentsTo(generator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        scheduler.start();
        infoServer.start();
    }

    public void stop() {
        infoServer.stop();
        try {
            scheduler.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int port() {
        return infoServer.getBase().getPort();
    }
}
