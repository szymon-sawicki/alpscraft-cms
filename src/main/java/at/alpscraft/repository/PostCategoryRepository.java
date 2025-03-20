package at.alpscraft.repository;

import at.alpscraft.domain.PostCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {}
