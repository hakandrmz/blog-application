package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.exception.BlogAPIUnauthorizedException;
import guru.hakandurmaz.blog.exception.ResourceNotFoundException;
import guru.hakandurmaz.blog.payload.post.CreatePostRequest;
import guru.hakandurmaz.blog.payload.post.GetPostByIdDto;
import guru.hakandurmaz.blog.payload.post.GetPostDto;
import guru.hakandurmaz.blog.payload.post.PostRequest;
import guru.hakandurmaz.blog.payload.post.UpdatePostRequest;
import guru.hakandurmaz.blog.repository.PostRepository;
import guru.hakandurmaz.blog.service.PostService;
import guru.hakandurmaz.blog.service.UserService;
import guru.hakandurmaz.blog.utils.mappers.ModelMapperService;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.ErrorResult;
import guru.hakandurmaz.blog.utils.results.Result;
import guru.hakandurmaz.blog.utils.results.SuccessDataResult;
import guru.hakandurmaz.blog.utils.results.SuccessResult;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserService userService;
  private final ModelMapperService modelMapperService;

  public PostServiceImpl(
      PostRepository postRepository,
      UserService userService,
      ModelMapperService modelMapperService) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.modelMapperService = modelMapperService;
  }

  @Override
  public Result createPost(CreatePostRequest createPostRequest, String username) {

    if (postRepository.existsByTitleAndUser_Username(createPostRequest.getTitle(), username)) {
      return new ErrorResult("This title is exist");
    } else {
      Post post = this.modelMapperService.forRequest().map(createPostRequest, Post.class);
      User user = userService.getUserByEmail(username);
      post.setUser(user);
      this.postRepository.save(post);
      return new SuccessResult("Post is published");
    }
  }

  @Override
  public DataResult<GetPostDto> getAllPosts(
      String username, int pageNo, int pageSize, String sortBy, String sortDir) {
    Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

    Page<Post> posts =
        StringUtils.isNotBlank(username)
            ? postRepository.findAllByUser_Username(username, pageable)
            : postRepository.findAll(pageable);

    GetPostDto response = this.configureResponse(posts);
    return new SuccessDataResult<>(response);
  }

  private static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
    Sort sort =
        sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    return PageRequest.of(pageNo, pageSize, sort);

  }

  @Override
  public DataResult<GetPostByIdDto> getPostById(long id) {

    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    return new SuccessDataResult<>(modelMapperService.forDto().map(post, GetPostByIdDto.class));
  }

  @Override
  public Result updatePost(UpdatePostRequest postRequest, String username) {
    Post post = checkPostAuthentication(postRequest.getId(), username);
    post.setDescription(postRequest.getDescription());
    post.setContent(postRequest.getContent());
    post.setTitle(postRequest.getTitle());
    postRepository.save(post);
    return new SuccessResult("Post updated");
  }

  @Override
  public Result deletePostById(long id, String username) {
    checkPostAuthentication(id, username);
    postRepository.deleteById(id);
    return new SuccessResult("Post deleted");
  }

  @Override
  public GetPostDto listOfPosts(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
    Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);
    Page<Post> posts = postRepository.findByTitleContainingIgnoreCase(query, pageable);
    return configureResponse(posts);
  }

  private GetPostDto configureResponse(Page<Post> posts) {
    List<Post> listOfPosts = posts.getContent();
    List<PostRequest> content =
        listOfPosts.stream()
            .map(post -> modelMapperService.forDto().map(post, PostRequest.class))
            .collect(Collectors.toList());
    GetPostDto getPostDto = new GetPostDto();
    getPostDto.setContent(content);
    getPostDto.setPageNo(posts.getNumber());
    getPostDto.setPageSize(posts.getSize());
    getPostDto.setTotalElements(posts.getTotalElements());
    getPostDto.setTotalPages(posts.getTotalPages());
    getPostDto.setLast(posts.isLast());
    return getPostDto;
  }

  private Post checkPostAuthentication(Long id, String username) {
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    if (!StringUtils.equals(post.getUser().getEmail(), username)) {
      throw new BlogAPIUnauthorizedException("You dont have permission.");
    }
    return post;
  }
}
