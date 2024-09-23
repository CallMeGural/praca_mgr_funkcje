package pl.gornickifilip.postsfunction;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@SpringBootApplication
public class PostsFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostsFunctionApplication.class, args);
    }

    private final PostRepository postRepository;
    private final Gson gson;

    public PostsFunctionApplication(PostRepository postRepository, Gson gson) {
        this.postRepository = postRepository;
        this.gson = gson;
    }

    @Bean
    public Function<String, Object> getPostsByUser() {
        return req -> {
            try {
                UUID userId = UUID.fromString(req);
                List<Post> posts = postRepository.findAllByUserId(userId);
                return gson.toJson(posts);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
