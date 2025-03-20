package at.alpscraft.repository;

import at.alpscraft.domain.UiSection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UiSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UiSectionRepository extends JpaRepository<UiSection, Long> {}
