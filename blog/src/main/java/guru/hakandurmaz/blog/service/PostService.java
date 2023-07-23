package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.payload.post.CreatePostRequest;
import guru.hakandurmaz.blog.payload.post.GetPostByIdDto;
import guru.hakandurmaz.blog.payload.post.GetPostDto;
import guru.hakandurmaz.blog.payload.post.UpdatePostRequest;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;

public interface PostService {

  Result createPost(CreatePostRequest createPostRequest, String username);

  DataResult<GetPostDto> getAllPosts(
      String username, int pageNo, int pageSize, String sortBy, String sortDir);

  DataResult<GetPostByIdDto> getPostById(long id);

  Result updatePost(UpdatePostRequest postRequest, String username);

  Result deletePostById(long id, String username);

  DataResult listOfPosts(String query);
}
