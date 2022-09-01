package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.payload.post.*;
import guru.hakandurmaz.blog.repository.PostRepository;
import guru.hakandurmaz.blog.service.PostService;
import guru.hakandurmaz.blog.utils.mappers.ModelMapperService;
import guru.hakandurmaz.blog.utils.results.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapperService modelMapperService;

    public PostServiceImpl(PostRepository postRepository,ModelMapperService modelMapperService) {
        this.postRepository = postRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public Result createPost(CreatePostRequest createPostRequest) {

        if(postRepository.existsByTitle(createPostRequest.getTitle())) {
            return new ErrorResult("This title is exist");
        }else {
            Post post = this.modelMapperService.forRequest().map(createPostRequest, Post.class);
            this.postRepository.save(post);
            return new SuccessResult("Post is published");
        }
    }

    @Override
    public DataResult<GetPostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        GetPostDto response = this.configureResponse(posts);

        return new SuccessDataResult<>(response);
    }

    @Override
    public DataResult getPostById(long id) {
        if(postRepository.existsById(id)) {
            Post post = postRepository.getById(id);
            return new SuccessDataResult(modelMapperService.forDto().map(post,GetPostByIdDto.class));
        }else {
            return new ErrorDataResult<>("This post is not found");
        }
    }

    @Override
    public Result updatePost(UpdatePostRequest postRequest) {
        if(postRepository.existsById(postRequest.getId())) {
            Post post = postRepository.getById(postRequest.getId());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            post.setTitle(postRequest.getTitle());
            postRepository.save(post);
            return new SuccessResult("Post is updated");
        }else {
            return new ErrorResult("This post is not found");
        }
    }

    @Override
    public Result deletePostById(long id) {
        if(postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return new SuccessResult("Post is deleted");
        }else {
            return new ErrorResult("This post is not found");
        }
    }

    @Override
    public DataResult listOfPosts(String query) {
        List<Post> searchPosts = postRepository.searchPosts(query);
        return new SuccessDataResult(searchPosts);
    }

    private GetPostDto configureResponse(Page<Post> posts) {
        List<Post> listOfPosts = posts.getContent();
        List<PostRequest> content = listOfPosts.stream().map(post -> modelMapperService.forDto().map(post,PostRequest.class)).collect(Collectors.toList());
        GetPostDto getPostDto = new GetPostDto();
        getPostDto.setContent(content);
        getPostDto.setPageNo(posts.getNumber());
        getPostDto.setPageSize(posts.getSize());
        getPostDto.setTotalElements(posts.getTotalElements());
        getPostDto.setTotalPages(posts.getTotalPages());
        getPostDto.setLast(posts.isLast());
        return getPostDto;
    }
}












