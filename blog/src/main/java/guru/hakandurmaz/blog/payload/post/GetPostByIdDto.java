package guru.hakandurmaz.blog.payload.post;

import guru.hakandurmaz.blog.payload.comment.CommentRequest;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPostByIdDto {

  private Long id;
  private String title;
  private String description;
  private String content;
  private Set<CommentRequest> comments;
  private Instant createdOn;
  private Instant lastUpdatedOn;
  private String updatedBy;
}
