package at.alpscraft.repository;

import at.alpscraft.domain.StaticPage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StaticPage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StaticPageRepository extends JpaRepository<StaticPage, Long> {
    @Query("select staticPage from StaticPage staticPage where staticPage.author.login = ?#{authentication.name}")
    List<StaticPage> findByAuthorIsCurrentUser();
}
