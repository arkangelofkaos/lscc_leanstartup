package com.timgroup.blankapp;

import com.timgroup.structuredevents.StructuredEventMatcher;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import static com.timgroup.blankapp.App.AppName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class AppTest extends IntegrationTest {
    @Test public void
    status_page_contains_app_name() throws Exception {
        execute(new HttpGet(server.uri("/info/status")));
        assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertThat(EntityUtils.toString(response.getEntity()), containsString(AppName));
    }

    @Test public void
    emits_application_started_event() throws Exception {
        assertThat(server.events(), hasItem(StructuredEventMatcher.ofType("ApplicationStarted")));
    }
}
