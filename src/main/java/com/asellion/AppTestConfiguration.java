package com.asellion;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.asellion")
@Profile("test")
@PropertySource(value = {"classpath:application-test.yml"})
public class AppTestConfiguration {

}
