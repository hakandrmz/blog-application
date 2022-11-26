package guru.hakandurmaz.blog.payload.comment;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {

  private long id;
  @NotEmpty(message = "Name should not be null or empty.")
  @Size(min = 2)
  private String name;
  @NotEmpty
  @Email(message = "Email format is not valid.")
  private String email;
  @NotEmpty
  @Size(min = 10, message = "min 10 character.")
  private String body;
}
