package pl.gornickifilip.registerfunction;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.util.function.Function;

@SpringBootApplication
@RequiredArgsConstructor
public class RegisterFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterFunctionApplication.class, args);
    }

    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;

    @Bean
    public Function<Object, String> register() {
        return req -> {
            try {
                BufferedReader reader = (BufferedReader) req;
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                ObjectMapper mapper = new ObjectMapper();
                AuthRequest request = mapper.readValue(sb.toString(), AuthRequest.class);
                if (userRepository.existsByEmail(request.getEmail())) {
                    throw new RuntimeException("Email already taken");
                }
                String encodedPassword = securityConfig.encoder().encode(request.getPassword());
                User user = User.builder()
                        .email(request.getEmail())
                        .password(encodedPassword)
                        .username(request.getUsername())
                        .build();
                userRepository.save(user);
                return "User Created";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
