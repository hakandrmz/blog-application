package guru.hakandurmaz.blog.payload.post;

import guru.hakandurmaz.blog.payload.comment.CommentRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class PostRequest {

  private Long id;

  @NotEmpty
  @Size(min = 2, message = "Post title should have at least 2 characters.")
  private String title;

  @NotEmpty
  @Size(min = 10, message = "Post description should have at least 10 characters.")
  private String description;

  @NotEmpty private String content;
  private Set<CommentRequest> comments;
}
