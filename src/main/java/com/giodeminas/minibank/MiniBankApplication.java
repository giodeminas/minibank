package com.giodeminas.minibank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MiniBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(MiniBankApplication.class, args);
  }

}
