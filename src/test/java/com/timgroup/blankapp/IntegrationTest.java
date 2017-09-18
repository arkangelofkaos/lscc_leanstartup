package com.timgroup.blankapp;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Rule;

public abstract class IntegrationTest {
    @Rule
    public final ServerRule server = new ServerRule();

    protected HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_0, HttpStatus.SC_INTERNAL_SERVER_ERROR, "No request executed");
    protected final HttpClientContext httpContext = new HttpClientContext();

    protected final CloseableHttpClient httpClient = HttpClients.custom()
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

    protected void execute(HttpUriRequest request) throws IOException {
        response = httpClient.execute(request, rawResponse -> {
            BasicHttpResponse bufferedResponse = new BasicHttpResponse(rawResponse.getStatusLine());
            for (Header header : rawResponse.getAllHeaders()) {
                bufferedResponse.addHeader(header);
            }
            if (rawResponse.getEntity() != null) {
                byte[] bytes = EntityUtils.toByteArray(rawResponse.getEntity());
                ByteArrayEntity bufferedEntity = new ByteArrayEntity(bytes);
                bufferedEntity.setContentType(rawResponse.getEntity().getContentType());
                bufferedResponse.setEntity(bufferedEntity);
            }
            return bufferedResponse;
        }, httpContext);
    }
}
