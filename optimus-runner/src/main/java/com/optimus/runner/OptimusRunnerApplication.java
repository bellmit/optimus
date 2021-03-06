package com.optimus.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OptimusRunnerApplication
 * 
 * @author sunxp
 */
@SpringBootApplication(scanBasePackages = "com.optimus")
public class OptimusRunnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusRunnerApplication.class, args);
	}

}
