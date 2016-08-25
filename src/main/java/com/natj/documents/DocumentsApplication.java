package com.natj.documents;

import com.natj.documents.repositories.TemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DocumentsApplication {
	private static final Logger log = LoggerFactory.getLogger(DocumentsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DocumentsApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TemplateRepository templateRepository) {
		return (args) -> {
//			templateRepository.save(new Template("Template 1"));
//			templateRepository.save(new Template("Template 2"));
//			templateRepository.save(new Template("Template 3"));
//			templateRepository.save(new Template("Template 3"));
//			templateRepository.save(new Template("Template 5"));
		};
	}
}
