package at.alpscraft.repository;

import at.alpscraft.domain.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FileStorage entity.
 */
@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {}
