package pl.gornickifilip.addpostfunction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;

@SpringBootApplication
public class AddPostFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddPostFunctionApplication.class, args);
    }
    private final String URL = "https://us-west1-imposing-cinema-432208-e8.cloudfunctions.net/get-user-function";
    private final PostRepository postRepository;
    private final Gson gson;

    public AddPostFunctionApplication(PostRepository postRepository, Gson gson) {
        this.postRepository = postRepository;
        this.gson = gson;
    }

    @Bean
    public Function<Object, Object> createPost() {
        return req -> {
          try {
              BufferedReader reader = (BufferedReader) req;
              StringBuilder sb = new StringBuilder();
              String line;
              while ((line = reader.readLine()) != null) {
                  sb.append(line);
              }
              ObjectMapper mapper = new ObjectMapper();
              PostDto request = mapper.readValue(sb.toString(), PostDto.class);
              ResponseEntity<UserResponseDto> userDetailsResponse = retrieveUserFromUserService(request.getUserId());

              Post post = Post.builder()
                      .title(request.getTitle())
                      .content(request.getContent())
                      .userId(userDetailsResponse.getBody().getId())
                      .comments(new ArrayList<>())
                      .createdAt(LocalDateTime.now())
                      .numberOfViews(0)
                      .build();
              postRepository.save(post);
              return gson.toJson(request);
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
        };
    }

    private ResponseEntity<UserResponseDto> retrieveUserFromUserService(UUID userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(userId.toString(), headers);

        return new RestTemplate()
                .exchange(URL, HttpMethod.POST, entity, UserResponseDto.class);
    }

}
