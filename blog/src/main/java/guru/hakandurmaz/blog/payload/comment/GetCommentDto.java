package guru.hakandurmaz.blog.payload.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentDto {
    private long id;
    private String name;
    private String email;
    private String body;
}
