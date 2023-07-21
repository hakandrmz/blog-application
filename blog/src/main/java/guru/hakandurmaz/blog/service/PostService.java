package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.payload.post.CreatePostRequest;
import guru.hakandurmaz.blog.payload.post.GetPostDto;
import guru.hakandurmaz.blog.payload.post.UpdatePostRequest;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;

public interface PostService {

  Result createPost(CreatePostRequest createPostRequest);

  DataResult<GetPostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

  DataResult<GetPostDto> getPostById(long id);

  Result updatePost(UpdatePostRequest postRequest);

  Result deletePostById(long id);

  DataResult listOfPosts(String query);
}
