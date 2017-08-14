package com.timgroup.blankapp;

import com.timgroup.structuredevents.StructuredEventMatcher;
import org.junit.Test;

import static com.timgroup.blankapp.App.AppName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;

public class LauncherTest extends IntegrationTest {
    @Test public void
    status_page_contains_app_name() throws Exception {
        assertThat(read(server.uri("/info/status")), hasItem(containsString(AppName)));
    }

    @Test public void
    emits_application_started_event() throws Exception {
        assertThat(server.events(), hasItem(StructuredEventMatcher.ofType("ApplicationStarted")));
    }
}
