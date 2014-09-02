package com.fintek.ets.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * A class specifying the application context resource.
 * @author sjamwal
 *
 */
@Configuration
@ImportResource( {"classpath*:/applicationContext.xml" } )
@ComponentScan
public class AppConfig {	

}
