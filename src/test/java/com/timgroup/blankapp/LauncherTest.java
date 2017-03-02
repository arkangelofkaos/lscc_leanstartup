package com.timgroup.blankapp;

import com.timgroup.structuredevents.StructuredEventMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static com.timgroup.blankapp.App.AppName;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;

public class LauncherTest {
    @Rule
    public final ServerRule server = new ServerRule();

    @Test public void
    status_page_contains_app_name() throws Exception {
        assertThat(read(server.uri("/info/status")), hasItem(containsString(AppName)));
    }

    @Test public void
    emits_application_started_event() throws Exception {
        assertThat(server.events(), hasItem(StructuredEventMatcher.ofType("ApplicationStarted")));
    }

    private List<String> read(URI uri) throws IOException {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(uri.toURL().openConnection().getInputStream(), UTF_8))) {
            return in.lines().collect(toList());
        }
    }
}
