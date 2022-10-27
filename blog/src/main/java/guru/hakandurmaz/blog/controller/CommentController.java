package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.payload.comment.CreateCommentRequest;
import guru.hakandurmaz.blog.payload.comment.GetCommentDto;
import guru.hakandurmaz.blog.payload.comment.UpdateCommentRequest;
import guru.hakandurmaz.blog.service.CommentService;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;
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

    @PostMapping("posts/comments/{postId}")
    public Result createComment(@PathVariable(value = "postId") long id,
                                @Valid @RequestBody CreateCommentRequest commentRequest){
        return commentService.createComment(id, commentRequest);
    }

    @GetMapping("posts/{postId}/comments")
    public DataResult<List<GetCommentDto>> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return this.commentService.getCommentsByPostId(postId);
    }

    @GetMapping("comments/{id}")
    public DataResult<GetCommentDto> getCommentById(@PathVariable(value = "id") Long commentId){
        return this.commentService.getCommentById(commentId);
    }

    @PutMapping("comments/{id}")
    public Result updateComment(@Valid @RequestBody UpdateCommentRequest commentRequest){
        return this.commentService.updateComment(commentRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("comments/{id}")
    public Result deleteComment(@PathVariable(value = "id") Long commentId){
        return this.commentService.deleteComment(commentId);
    }

}
