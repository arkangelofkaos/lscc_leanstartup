package com.timgroup.blankapp;

import java.time.Duration;
import java.util.List;

import com.timgroup.tucker.info.Component;
import com.timgroup.tucker.info.async.AsyncComponentScheduler;
import com.timgroup.tucker.info.component.JarVersionComponent;
import com.timgroup.tucker.info.servlet.ApplicationInformationServlet;
import com.timgroup.tucker.info.status.StatusPageGenerator;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import static com.timgroup.tucker.info.Health.ALWAYS_HEALTHY;
import static com.timgroup.tucker.info.Stoppable.ALWAYS_STOPPABLE;
import static com.timgroup.tucker.info.async.AsyncComponentScheduler.createFromSynchronous;
import static com.timgroup.tucker.info.async.AsyncSettings.settings;

public class JettyService {
    private final Server infoServer;
    private final AsyncComponentScheduler scheduler;

    public JettyService(String name, int port, List<Component> components) {
        StatusPageGenerator generator = new StatusPageGenerator(name, new JarVersionComponent(getClass()));
        this.scheduler = createFromSynchronous(components, settings().withRepeatSchedule(Duration.ofSeconds(10)));
        scheduler.addComponentsTo(generator);

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("Jetty");
        this.infoServer = new Server(threadPool);

        ServerConnector connector = new ServerConnector(infoServer);
        connector.setPort(port);
        infoServer.addConnector(connector);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder("tucker", new ApplicationInformationServlet(generator, ALWAYS_STOPPABLE, ALWAYS_HEALTHY)), "/info/*");

        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setHandler(servletContextHandler);

        StatisticsHandler statisticsHandler = new StatisticsHandler();
        statisticsHandler.setHandler(gzipHandler);

        infoServer.setHandler(statisticsHandler);
    }

    public void start() {
        scheduler.start();
        try {
            infoServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            infoServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            scheduler.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int port() {
        return ((NetworkConnector) infoServer.getConnectors()[0]).getLocalPort();
    }
}
