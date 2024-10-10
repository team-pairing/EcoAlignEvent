package kr.ac.kopo.ecoalignbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EcoAlignBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoAlignBackEndApplication.class, args);
	}

}
