package hanno0no.hnn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HnnApplication {
	// Java 프로그램의 main() 함수가 있는 진입 클래스이고, Spring Boot가 모든 걸 자동으로 구성하게 만드는 출발점

	public static void main(String[] args) {
		SpringApplication.run(HnnApplication.class, args);
	}
}
