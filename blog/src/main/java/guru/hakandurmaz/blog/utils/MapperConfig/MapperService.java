package guru.hakandurmaz.blog.utils.MapperConfig;

import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.payload.post.PostRequest;

public interface MapperService {
    Post mapToEntity(PostRequest postRequest);
    PostRequest mapToDto(Post post);
}
