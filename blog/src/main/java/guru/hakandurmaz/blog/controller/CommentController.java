package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.payload.comment.CommentRequest;
import guru.hakandurmaz.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<CommentRequest> createComment(@PathVariable(value = "postId") long id,
                                                        @Valid @RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentService.createComment(id, commentRequest), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentRequest> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentRequest> getCommentById(@PathVariable(value = "postId") Long postId,
                                                         @PathVariable(value = "id") Long commentId){
        CommentRequest commentRequest = commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentRequest,HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentRequest> updateComment(@PathVariable(value = "postId") Long postId,
                                                        @PathVariable(value = "id") Long commentId,
                                                        @Valid @RequestBody CommentRequest commentRequest){
        CommentRequest updatedComment = commentService.updateComment(postId,commentId, commentRequest);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "id") Long commentId){
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted succesfully",HttpStatus.OK);
    }

}
