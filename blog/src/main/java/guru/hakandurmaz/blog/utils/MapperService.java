package guru.hakandurmaz.blog.utils;

import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.payload.post.PostRequest;

public interface MapperService {
    Post mapToEntity(PostRequest postRequest);
    PostRequest mapToDto(Post post);
}