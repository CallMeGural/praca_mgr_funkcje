package pl.gornickifilip.postsfunction;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}