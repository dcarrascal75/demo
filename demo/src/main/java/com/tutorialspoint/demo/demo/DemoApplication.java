package com.tutorialspoint.demo.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@RefreshScope

public class DemoApplication implements ApplicationRunner{

	
	@Value("${spring.application.name}")
	private String nombre;
	
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
	}
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
	System.out.println("Hello World from Application Runner " + nombre);
	}
	
	
	@Bean
	public RestTemplate getRestTemplate() {
	   return new RestTemplate();
	}

}
