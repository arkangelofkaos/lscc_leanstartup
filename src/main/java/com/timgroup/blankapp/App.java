package com.timgroup.blankapp;

import com.timgroup.blankapp.monitoring.StatusPage;
import com.timgroup.tucker.info.component.JvmVersionComponent;
import com.typesafe.config.Config;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;

public class App {
    public static final String AppName = "blank-app";
    private StatusPage statusPage;

    public App(Config config) {
        statusPage = new StatusPage(AppName,
                                    config.getInt("port"),
                                    asList(new JvmVersionComponent()));

    }

    public void start() {
        statusPage.start();
        LoggerFactory.getLogger(Launcher.class).info("Started " + AppName);
    }

    public void stop() {
        statusPage.stop();
    }
}



