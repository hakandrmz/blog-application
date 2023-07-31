package guru.hakandurmaz.blog.payload.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentDto {

  private long id;
  private String name;
  private String email;
  private String body;
  private Instant createdOn;
  private Instant lastUpdatedOn;
}
