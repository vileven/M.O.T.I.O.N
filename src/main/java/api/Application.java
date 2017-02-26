package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();
        SpringApplication.run(Application.class, args);
    }

}