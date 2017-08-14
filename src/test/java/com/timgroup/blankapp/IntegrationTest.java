package com.timgroup.blankapp;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;

public abstract class IntegrationTest {
    @Rule
    public final ServerRule server = new ServerRule();

    public final CloseableHttpClient httpClient = HttpClients.custom()
            .setRoutePlanner((target, request, context) -> {
                HttpHost serverHost = new HttpHost("localhost", server.app().port());
                if (target == null || target.equals(serverHost)) {
                    return new HttpRoute(serverHost);
                }
                throw new IllegalStateException("Refusing to route request outside server under test: " + target + " for " + request);
            })
            .build();

    @After
    public void closeHttpClient() {
        try {
            httpClient.close();
        } catch (IOException ignored) {
        }
    }

    public List<String> read(URI uri) throws IOException {
        return httpClient.execute(new HttpGet(uri), response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                Assert.fail(response.getStatusLine().toString());
            }
            return Arrays.asList(EntityUtils.toString(response.getEntity()).split("\n"));
        });
    }
}
