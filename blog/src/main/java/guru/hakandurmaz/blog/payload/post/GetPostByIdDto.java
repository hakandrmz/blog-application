package guru.hakandurmaz.blog.payload.post;

import guru.hakandurmaz.blog.payload.comment.CommentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPostByIdDto {
    private Long id;
    private String title;
    private String description;
    private String content;
    private Set<CommentRequest> comments;
}
