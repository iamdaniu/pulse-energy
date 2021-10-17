package de.daniu.pulseenergy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@EnableConfigurationProperties
@SpringBootApplication
public class PulseEnergyApplication {
	public static void main(String[] args) {
		SpringApplication.run(PulseEnergyApplication.class, args);
	}
}
