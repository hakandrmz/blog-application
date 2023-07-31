package guru.hakandurmaz.blog.repository;

import guru.hakandurmaz.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  boolean existsByTitleAndUser_Username(String title, String username);

  boolean existsById(Long id);

  Page<Post> findAllByUser_Username(String username, Pageable pageable);

  Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
