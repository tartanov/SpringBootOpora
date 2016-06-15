package com.example;

import com.example.config.MyServletConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@Import(MyServletConfig.class)
@ImportResource("classpath:/config/StaticConfig.xml")
public class SpringBootCamelCxfExposeSoapv1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCamelCxfExposeSoapv1Application.class, args);
	}
}
