package com.timgroup.blankapp;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class Metrics {
    private final MetricRegistry registry;

    private final Optional<GraphiteReporter> graphiteReporter;

    public Metrics(MetricsConfig config) {
        registry = new MetricRegistry();
        graphiteReporter = makeReporter(config);
    }

    private Optional<GraphiteReporter> makeReporter(MetricsConfig config) {
        if (config.enabled()) {
            final Graphite graphite = new Graphite(new InetSocketAddress(config.host(), config.port()));
            return Optional.of(GraphiteReporter.forRegistry(registry).prefixedWith(config.prefix()).build(graphite));
        }
        return Optional.empty();
    }

    public void start() {
        graphiteReporter.ifPresent(new Consumer<GraphiteReporter>() {
            @Override public void accept(GraphiteReporter graphiteReporter) {
                graphiteReporter.start(10, TimeUnit.SECONDS);
            }
        });
    }

    public void stop() {
        graphiteReporter.ifPresent(new Consumer<GraphiteReporter>() {
            @Override public void accept(GraphiteReporter graphiteReporter) {
                graphiteReporter.stop();
            }
        });
    }

    public void addJvmMetrics() {
        registry.register("jvm", new JvmAttributeGaugeSet());
        registry.register("jvm.fd_usage", new FileDescriptorRatioGauge());
        registry.register("jvm.gc", new GarbageCollectorMetricSet());
        registry.register("jvm.memory", new MemoryUsageGaugeSet());
        registry.register("jvm.thread-states", new ThreadStatesGaugeSet());
    }
}
