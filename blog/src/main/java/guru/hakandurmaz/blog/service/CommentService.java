package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.payload.comment.CreateCommentRequest;
import guru.hakandurmaz.blog.payload.comment.GetCommentDto;
import guru.hakandurmaz.blog.payload.comment.UpdateCommentRequest;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;
import java.util.List;

public interface CommentService {

  Result createComment(long id, CreateCommentRequest commentRequest, String username);

  DataResult<List<GetCommentDto>> getCommentsByPostId(long postId);

  DataResult<GetCommentDto> getCommentById(Long commentId);

  Result updateComment(UpdateCommentRequest commentRequest, String username);

  Result deleteComment(Long commentId, String username);
}
