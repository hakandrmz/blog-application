package guru.hakandurmaz.blog.repository;

import guru.hakandurmaz.blog.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

  boolean existsByTitle(String title);

  boolean existsById(Long id);

  @Query("SELECT p FROM Post p WHERE " +
      "p.title = ?1" +
      "Or p.description = ?1")
  List<Post> searchPosts(String query);

}
