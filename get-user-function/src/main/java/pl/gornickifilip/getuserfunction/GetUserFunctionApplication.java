package pl.gornickifilip.getuserfunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.function.Function;

@SpringBootApplication
public class GetUserFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetUserFunctionApplication.class, args);
    }
    @Bean
    public Function<String,Object> getUserById(UserRepository userRepository) {
        return req -> {
            try {
                UUID userId = UUID.fromString(req);
                return userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
