package com.timgroup.securefilesender;

public interface MetricsConfig {
    boolean enabled();
    String host();
    int port();
    String prefix();
}
