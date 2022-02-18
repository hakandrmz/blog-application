package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.payload.comment.CommentRequest;

import java.util.List;

public interface CommentService {
    CommentRequest createComment(long id, CommentRequest commentRequest);
    List<CommentRequest> getCommentsByPostId(long PostId);
    CommentRequest getCommentById(Long postId, Long commentId);
    CommentRequest updateComment(Long postId, Long commentId, CommentRequest commentRequest);
    void deleteComment(Long postId,Long commentId);
}
