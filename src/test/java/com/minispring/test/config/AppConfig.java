package com.minispring.test.config;

import com.minispring.annotation.ComponentScan;

/**
 * Application configuration class.
 */
@ComponentScan(basePackages = "com.minispring.test")
public class AppConfig {
    // Configuration class can be empty, the @ComponentScan annotation does the work
}