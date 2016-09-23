package com.timgroup.blankapp;

public interface MetricsConfig {
    boolean enabled();
    String host();
    int port();
    String prefix();
}
