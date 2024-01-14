package information.portal;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("information.portal")
public class SqlitedemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SqlitedemoApplication.class, args);
	}
}

