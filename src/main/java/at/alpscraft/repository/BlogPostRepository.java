package at.alpscraft.repository;

import at.alpscraft.domain.BlogPost;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BlogPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    @Query("select blogPost from BlogPost blogPost where blogPost.author.login = ?#{authentication.name}")
    List<BlogPost> findByAuthorIsCurrentUser();
}
