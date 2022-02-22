package guru.hakandurmaz.blog.repository;

import guru.hakandurmaz.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    boolean existsByTitle(String title);
    boolean existsById(Long id);
}
