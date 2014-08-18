package com.fintek.ets.application;

import java.io.FileNotFoundException;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import quickfix.ConfigError;

import com.fintek.ets.connectivity.QFJtest;

@Service
@Configuration
@ComponentScan("com.fintek")
public class Application {
	
	public static void main(String[] args) throws FileNotFoundException, ConfigError {
		
		
        QFJtest test = new QFJtest();
		test.start();
	}
	
	
	public void init() throws FileNotFoundException, ConfigError {
		System.out.println("Starting QFJtest.....");
		QFJtest test = new QFJtest();
		test.start();		
	}

}
