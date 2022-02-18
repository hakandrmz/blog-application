package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.Comment;
import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.exception.BlogAPIException;
import guru.hakandurmaz.blog.exception.ResourceNotFoundException;
import guru.hakandurmaz.blog.payload.comment.CommentRequest;
import guru.hakandurmaz.blog.repository.CommentRepository;
import guru.hakandurmaz.blog.repository.PostRepository;
import guru.hakandurmaz.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    //DI
    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentRequest createComment(long id, CommentRequest commentRequest) {

        Comment comment = mapToEntity(commentRequest);
        //retrieve post entity by id
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",id));
        //set post to comment entity
        comment.setPost(post);
        //save comment to entity to database
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public List<CommentRequest> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert list of comment to comment dto list
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentRequest getCommentById(Long postId, Long commentId) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        return mapToDto(comment);

    }

    @Override
    public CommentRequest updateComment(Long postId, Long commentId, CommentRequest commentRequest) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);

    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        commentRepository.delete(comment);

    }


    private CommentRequest mapToDto(Comment comment){
        CommentRequest commentRequest = mapper.map(comment, CommentRequest.class);
        /*
        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        */
        return commentRequest;
    }
    private Comment mapToEntity(CommentRequest commentRequest){
        Comment comment = mapper.map(commentRequest,Comment.class);
        /*
        comment.setId(commentDto.getId());
        comment.setBody(commentDto.getBody());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        */
        return comment;
    }
}
