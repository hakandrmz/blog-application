package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.Comment;
import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.exception.BlogAPIUnauthorizedException;
import guru.hakandurmaz.blog.exception.ResourceNotFoundException;
import guru.hakandurmaz.blog.payload.comment.CreateCommentRequest;
import guru.hakandurmaz.blog.payload.comment.GetCommentDto;
import guru.hakandurmaz.blog.payload.comment.UpdateCommentRequest;
import guru.hakandurmaz.blog.repository.CommentRepository;
import guru.hakandurmaz.blog.repository.PostRepository;
import guru.hakandurmaz.blog.service.CacheService;
import guru.hakandurmaz.blog.service.CommentService;
import guru.hakandurmaz.blog.service.UserService;
import guru.hakandurmaz.blog.utils.mappers.ModelMapperService;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.ErrorDataResult;
import guru.hakandurmaz.blog.utils.results.ErrorResult;
import guru.hakandurmaz.blog.utils.results.Result;
import guru.hakandurmaz.blog.utils.results.SuccessDataResult;
import guru.hakandurmaz.blog.utils.results.SuccessResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapperService modelMapperService;
    private final UserService userService;

    @Override
    public Result createComment(long postId, CreateCommentRequest commentRequest, String username) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
            User user = userService.getUserByUserName(username);
            Comment comment = this.modelMapperService.forRequest().map(commentRequest, Comment.class);
            comment.setPost(post.get());
            comment.setUser(user);
            commentRepository.save(comment);
            return new SuccessResult("Comment saved.");
        } else {
            return new ErrorResult("Post is not exist");
        }
    }

    @Override
    public DataResult<List<GetCommentDto>> getCommentsByPostId(long postId) {
        if (postRepository.existsById(postId)) {
            List<Comment> comments = commentRepository.findByPostId(postId);
            List<GetCommentDto> getCommentDtos =
                    comments.stream()
                            .map(comment -> modelMapperService.forDto().map(comment, GetCommentDto.class))
                            .collect(Collectors.toList());
            return new SuccessDataResult<>(getCommentDtos);
        } else {
            return new ErrorDataResult<>("Post is not exist");
        }
    }

    @Override
    public DataResult<GetCommentDto> getCommentById(Long commentId) {

        Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        GetCommentDto getCommentDto = modelMapperService.forDto().map(comment, GetCommentDto.class);

        return new SuccessDataResult<>(getCommentDto);
    }

    @Override
    public Result updateComment(UpdateCommentRequest commentRequest, String username) {
        Comment comment = checkCommentAuthentication(commentRequest.getId(), username);

        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());
        commentRepository.save(comment);

        return new SuccessResult("Updated comment.");
    }

    @Override
    public Result deleteComment(Long commentId, String username) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return new SuccessResult("Comment is deleted");
        } else {
            return new ErrorResult("Comment is not exist");
        }
    }

    private Comment checkCommentAuthentication(long id, String username) {
        Comment comment =
                commentRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        if (!StringUtils.equals(comment.getUser().getEmail(), username)) {
            throw new BlogAPIUnauthorizedException("You dont have permission.");
        }
        return comment;
    }
}
