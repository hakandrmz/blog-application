package guru.hakandurmaz.blog.utils;

import guru.hakandurmaz.blog.entity.Post;
import guru.hakandurmaz.blog.payload.post.PostRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MapperManager implements MapperService {

    private ModelMapper mapper;

    public MapperManager(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Post mapToEntity(PostRequest postRequest) {
        Post post = mapper.map(postRequest,Post.class);
        return post;
    }

    @Override
    public PostRequest mapToDto(Post post) {
        PostRequest postRequest = mapper.map(post, PostRequest.class);
        return postRequest;
    }
}
