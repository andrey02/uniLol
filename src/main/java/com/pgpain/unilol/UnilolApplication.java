package com.pgpain.unilol;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;

@SpringBootApplication
@EnableAsync
public class UnilolApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UnilolApplication.class);
	}

	@Autowired
	private Environment env;

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(500);
		executor.initialize();
		return executor;
	}

	public static void main(String[] args) {
		SpringApplication.run(UnilolApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		Orianna.setRiotAPIKey(env.getProperty("API_KEY"));
		Orianna.setDefaultRegion(Region.BRAZIL);
	}

}
