package com.fintek.ets.application;

import org.springframework.context.annotation.Configuration;

import com.fintek.ets.connectivity.QFJtest;

@Configuration
public class Application {
	

	public QFJtest initQFJ() {
		System.out.println("Starting QFJtest.....");
		QFJtest test = new QFJtest();
		return test;
	}

}
