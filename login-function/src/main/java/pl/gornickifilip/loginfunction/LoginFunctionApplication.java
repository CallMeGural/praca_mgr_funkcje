package pl.gornickifilip.loginfunction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication
public class LoginFunctionApplication {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    public LoginFunctionApplication(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(LoginFunctionApplication.class, args);
    }

    @Bean
    public Function<Object, String> login() {
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


                Optional<User> user = userRepository.findByEmail(request.getEmail());
                if (user.isPresent() && securityConfig.encoder().matches(request.getPassword(), user.get().getPassword())) {
                    return String.valueOf(user.get().getId());
                } else {
                    throw new RuntimeException("Incorrect email or password");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        };
    }
}
