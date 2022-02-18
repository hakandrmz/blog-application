package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.payload.post.PostRequest;
import guru.hakandurmaz.blog.payload.post.PostResponse;

public interface PostService {
    PostRequest createPost(PostRequest postRequest);
    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);
    PostRequest getPostById(long id);
    PostRequest updatePost(PostRequest postRequest, long id);
    void deletePostById(long id);
}
