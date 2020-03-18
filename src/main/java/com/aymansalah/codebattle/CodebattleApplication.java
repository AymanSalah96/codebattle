package com.aymansalah.codebattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;

@SpringBootApplication
@ComponentScan
public class CodebattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodebattleApplication.class, args);
	}
}
