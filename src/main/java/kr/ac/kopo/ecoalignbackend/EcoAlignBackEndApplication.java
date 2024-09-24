package kr.ac.kopo.ecoalignbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EcoAlignBackEndApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // .env 파일 로드 (암호화 키 관리 파일)
		SpringApplication.run(EcoAlignBackEndApplication.class, args);
	}

}
