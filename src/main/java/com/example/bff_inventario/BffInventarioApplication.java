package com.example.bff_inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BffInventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffInventarioApplication.class, args);
	}

}
