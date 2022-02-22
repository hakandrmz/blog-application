package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.Comment;
import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.exception.BlogAPIException;
import guru.hakandurmaz.blog.exception.ResourceNotFoundException;
import guru.hakandurmaz.blog.payload.comment.CreateCommentRequest;
import guru.hakandurmaz.blog.payload.comment.GetCommentDto;
import guru.hakandurmaz.blog.payload.comment.CommentRequest;
import guru.hakandurmaz.blog.payload.comment.UpdateCommentRequest;
import guru.hakandurmaz.blog.repository.CommentRepository;
import guru.hakandurmaz.blog.repository.PostRepository;
import guru.hakandurmaz.blog.service.CommentService;
import guru.hakandurmaz.blog.utils.ModelMapperService;
import guru.hakandurmaz.blog.utils.results.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapperService modelMapperService;

    //DI
    public CommentServiceImpl(ModelMapperService modelMapperService,CommentRepository commentRepository,PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapperService = modelMapperService;

    }

    @Override
    public Result createComment(long postId, CreateCommentRequest commentRequest) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Comment comment = this.modelMapperService.forRequest().map(commentRequest,Comment.class);
            comment.setPost(post.get());
            commentRepository.save(comment);
            return new SuccessResult("Yorum Kaydedildi");
        }else {
            return new ErrorResult("Post is not exist");
        }
    }

    @Override
    public DataResult<List<GetCommentDto>> getCommentsByPostId(long postId) {
        if(postRepository.existsById(postId)) {
            List<Comment> comments = commentRepository.findByPostId(postId);
            List<GetCommentDto> getCommentDtos = comments.stream().map(comment -> modelMapperService.forDto().map(comment, GetCommentDto.class)).collect(Collectors.toList());
            return new SuccessDataResult<>(getCommentDtos);
        }else {
            return new ErrorDataResult<>("Post is not exist");
        }
    }

    //todo
    @Override
    public DataResult<GetCommentDto> getCommentById(Long commentId) {

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentId));

        GetCommentDto getCommentDto = modelMapperService.forDto().map(comment, GetCommentDto.class);

        return new SuccessDataResult<>(getCommentDto);

    }

    //TODO
    @Override
    public Result updateComment(UpdateCommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentRequest.getId()));

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
        commentRepository.save(comment);

        return new SuccessResult("Yorum güncellendi");

    }

    @Override
    public Result deleteComment(Long commentId) {
        if(commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return new SuccessResult("Comment is deleted");
        }else {
            return new ErrorResult("Comment is not exist");
        }
    }
}
