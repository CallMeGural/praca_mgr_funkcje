package pl.gornickifilip.addpostfunction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private UUID id;
    private String content;
    private String title;
    private UUID userId;
}
