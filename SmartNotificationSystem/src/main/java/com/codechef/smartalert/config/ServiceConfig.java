package com.codechef.smartalert.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.codechef.smartalert.notification.service")
public class ServiceConfig {

}
