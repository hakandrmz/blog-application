package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.payload.post.PostRequest;
import guru.hakandurmaz.blog.payload.post.PostResponse;
import guru.hakandurmaz.blog.service.PostService;
import guru.hakandurmaz.blog.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostRequest> createPost(@Valid @RequestBody PostRequest postRequest){
        return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.OK);
    }

    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        log.info(postService.getAllPosts(pageNo,pageSize,sortBy,sortDir).toString());
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<PostRequest> getPostByIdV1(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<PostRequest> updatePost(@ Valid @RequestBody PostRequest postRequest,
                                                  @PathVariable(name = "id") long id){
       PostRequest postResponse = postService.updatePost(postRequest,id);
       return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
       postService.deletePostById(id);
       return new ResponseEntity<>("Post entity deleted succesfully",HttpStatus.OK);
    }

}
