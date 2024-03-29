package guru.hakandurmaz.blog.payload.post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {

  private Long id;
  private String title;

  @NotEmpty
  @Size(min = 10, message = "Post description should have at least 10 characters.")
  private String description;

  @NotEmpty private String content;

}
