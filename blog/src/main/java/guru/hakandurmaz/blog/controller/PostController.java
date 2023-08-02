package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.config.LogPerformance;
import guru.hakandurmaz.blog.payload.post.CreatePostRequest;
import guru.hakandurmaz.blog.payload.post.GetPostByIdDto;
import guru.hakandurmaz.blog.payload.post.GetPostDto;
import guru.hakandurmaz.blog.payload.post.UpdatePostRequest;
import guru.hakandurmaz.blog.service.PostService;
import guru.hakandurmaz.blog.utils.constants.AppConstants;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;
import guru.hakandurmaz.blog.utils.results.SuccessDataResult;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/posts")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }
  @LogPerformance
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @PostMapping
  public Result createPost(
      @Valid @RequestBody CreatePostRequest postRequest, Authentication authentication) {
    String username = authentication.getName();
    return this.postService.createPost(postRequest, username);
  }
  
  @LogPerformance
  @GetMapping("/username")
  public DataResult<GetPostDto> getAllPostsByUsername(
      @RequestParam(name = "username", required = false) String username,
      @RequestParam(
              value = "pageNo",
              defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
              required = false)
          int pageNo,
      @RequestParam(
              value = "pageSize",
              defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
              required = false)
          int pageSize,
      @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)
          String sortBy,
      @RequestParam(
              value = "sortDir",
              defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,
              required = false)
          String sortDir) {
    return this.postService.getAllPosts(username, pageNo, pageSize, sortBy, sortDir);
  }
  @LogPerformance
  @GetMapping(value = "{id}")
  public DataResult<GetPostByIdDto> getPostById(@PathVariable(name = "id") long id) {
    return postService.getPostById(id);
  }
  
  @LogPerformance
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @PutMapping
  public Result updatePost(
      @Valid @RequestBody UpdatePostRequest postRequest, Authentication authentication) {
    String username = authentication.getName();
    return postService.updatePost(postRequest, username);
  }
  
  @LogPerformance
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @DeleteMapping("{id}")
  public Result deletePost(@PathVariable(name = "id") long id, Authentication authentication) {
    String username = authentication.getName();
    return postService.deletePostById(id, username);
  }

  @LogPerformance
  @GetMapping("/search")
  public DataResult<GetPostDto> searchPosts(
          @RequestParam(name = "keyword", required = false) String keyword,
          @RequestParam(
                  value = "pageNo",
                  defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
                  required = false)
          int pageNo,
          @RequestParam(
                  value = "pageSize",
                  defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
                  required = false)
          int pageSize,
          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)
          String sortBy,
          @RequestParam(
                  value = "sortDir",
                  defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,
                  required = false)
          String sortDir) {
    return new SuccessDataResult(postService.listOfPosts(keyword, pageNo, pageSize, sortBy, sortDir));
  }
}
