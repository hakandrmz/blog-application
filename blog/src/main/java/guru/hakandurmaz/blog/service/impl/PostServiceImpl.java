package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.exception.ResourceNotFoundException;
import guru.hakandurmaz.blog.payload.post.PostRequest;
import guru.hakandurmaz.blog.payload.post.PostResponse;
import guru.hakandurmaz.blog.repository.PostRepository;
import guru.hakandurmaz.blog.service.PostService;
import guru.hakandurmaz.blog.utils.MapperConfig.MapperService;
import guru.hakandurmaz.clients.notification.NotificationClient;
import guru.hakandurmaz.clients.notification.NotificationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private MapperService mapperService;

    public PostServiceImpl(PostRepository postRepository,MapperService mapperService) {
        this.postRepository = postRepository;
        this.mapperService = mapperService;
    }

    @Override
    public PostRequest createPost(PostRequest postRequest) {

        //convert dto to entity
        Post post = mapperService.mapToEntity(postRequest);
        Post newPost = postRepository.save(post);


        //convert entity to dto
        PostRequest postResponse = mapperService.mapToDto(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //create pagable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        //get content from page object
        List<Post> listOfPosts = posts.getContent();

        List<PostRequest> content = listOfPosts.stream().map(post -> mapperService.mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostRequest getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        return mapperService.mapToDto(post);
    }

    @Override
    public PostRequest updatePost(PostRequest postRequest, long id) {
        //get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));

        post.setDescription(postRequest.getDescription());
        post.setContent(postRequest.getContent());
        post.setTitle(postRequest.getTitle());

        Post updatedPost = postRepository.save(post);
        return mapperService.mapToDto(updatedPost);

    }

    @Override
    public void deletePostById(long id) {
        //get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

}












