package com.timgroup.blankapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.timgroup.structuredevents.standardevents.ApplicationStarted;
import org.junit.Rule;
import org.junit.Test;

import static com.timgroup.blankapp.App.AppName;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;

public class LauncherTest {
    @Rule
    public final ServerRule server = new ServerRule();

    @Test public void
    status_page_contains_app_name() throws Exception {
        assertThat(read("http://localhost:8000/info/status"), hasItem(containsString(AppName)));
    }

    @Test public void
    emits_application_started_event() throws Exception {
        assertThat(server.events(), hasItem(instanceOf(ApplicationStarted.class)));
    }

    private List<String> read(String url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));

        return in.lines().collect(toList());
    }
}
