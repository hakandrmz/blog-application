package guru.hakandurmaz.blog.payload.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

  @NotEmpty(message = "Name should not be null or empty.")
  @Size(min = 2)
  private String name;

  @NotEmpty
  @Size(min = 10, message = "min 10 character.")
  private String body;
}
