package guru.hakandurmaz.blog.payload.post;

import guru.hakandurmaz.blog.payload.comment.CommentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    @NotEmpty
    @Size(min = 2,message = "Post title should have at least 2 characters.")
    private String title;
    @NotEmpty
    @Size(min = 10,message = "Post description should have at least 10 characters.")
    private String description;
    @NotEmpty
    private String content;
}
