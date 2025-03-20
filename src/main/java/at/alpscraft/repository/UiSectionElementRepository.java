package at.alpscraft.repository;

import at.alpscraft.domain.UiSectionElement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UiSectionElement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UiSectionElementRepository extends JpaRepository<UiSectionElement, Long> {}
