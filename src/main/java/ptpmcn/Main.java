package ptpmcn;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ptpmcn.service.StorageService;

@SpringBootApplication
public class Main{
	
	@Resource
	StorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	
}

