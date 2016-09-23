package com.timgroup.blankapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import static com.timgroup.blankapp.App.AppName;
import static com.timgroup.blankapp.Launcher.loadConfig;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;

public class LauncherTest {
    private final App app = new App(loadConfig("config.properties"));

    @Before
    public void start() {
        app.start();
    }

    @After
    public void stop() {
        app.stop();
    }


    @Test public void
    status_page_contains_app_name() throws IOException {
        assertThat(read("http://localhost:8000/info/status"), hasItem(containsString(AppName)));
    }

    private List<String> read(String url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));

        return in.lines().collect(toList());
    }
}
