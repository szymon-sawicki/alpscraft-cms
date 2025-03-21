package at.alpscraft.service;

import at.alpscraft.domain.FileStorage;
import at.alpscraft.repository.FileStorageRepository;
import at.alpscraft.security.SecurityUtils;
import at.alpscraft.service.dto.FileStorageDTO;
import at.alpscraft.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for managing file storage.
 */
@Service
@Transactional
public class FileStorageService {

    private final Logger log = LoggerFactory.getLogger(FileStorageService.class);
    private final FileStorageRepository fileStorageRepository;
    private final UserService userService;

    public FileStorageService(FileStorageRepository fileStorageRepository, UserService userService) {
        this.fileStorageRepository = fileStorageRepository;
        this.userService = userService;
    }

    /**
     * Save a file to storage.
     *
     * @param file the file to save
     * @return the saved FileStorage
     */
    public FileStorage storeFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            byte[] data = file.getBytes();

            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileName(fileName);
            fileStorage.setFileType(fileType);
            fileStorage.setFileSize(file.getSize());
            fileStorage.setData(data);
            fileStorage.setUploadDate(Instant.now());

            // Set uploader if authenticated
            SecurityUtils.getCurrentUserLogin().flatMap(userService::getUserWithAuthoritiesByLogin).ifPresent(fileStorage::setUploader);

            return fileStorageRepository.save(fileStorage);
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new BadRequestAlertException("Failed to store file", "fileStorage", "fileError");
        }
    }

    /**
     * Store a file from a Base64 encoded string.
     *
     * @param base64Content the Base64 encoded content
     * @param fileName the name of the file
     * @param fileType the MIME type of the file
     * @return the saved FileStorage
     */
    public FileStorage storeBase64File(String base64Content, String fileName, String fileType) {
        try {
            // Remove the "data:image/jpeg;base64," prefix if present
            String base64Data = base64Content;
            if (base64Content.contains(",")) {
                base64Data = base64Content.split(",")[1];
            }

            byte[] data = Base64.getDecoder().decode(base64Data);

            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileName(fileName);
            fileStorage.setFileType(fileType);
            fileStorage.setFileSize((long) data.length);
            fileStorage.setData(data);
            fileStorage.setUploadDate(Instant.now());

            // Set uploader if authenticated
            SecurityUtils.getCurrentUserLogin().flatMap(userService::getUserWithAuthoritiesByLogin).ifPresent(fileStorage::setUploader);

            return fileStorageRepository.save(fileStorage);
        } catch (Exception e) {
            log.error("Failed to store base64 file", e);
            throw new BadRequestAlertException("Failed to store file", "fileStorage", "fileError");
        }
    }

    /**
     * Get a file by ID.
     *
     * @param id the ID of the file
     * @return the FileStorage
     */
    @Transactional(readOnly = true)
    public Optional<FileStorage> getFile(Long id) {
        return fileStorageRepository.findById(id);
    }

    /**
     * Delete a file.
     *
     * @param id the ID of the file to delete
     */
    public void deleteFile(Long id) {
        fileStorageRepository.deleteById(id);
    }

    /**
     * Create a DTO from a FileStorage entity
     *
     * @param fileStorage the entity
     * @return the DTO
     */
    public FileStorageDTO toDto(FileStorage fileStorage) {
        if (fileStorage == null) {
            return null;
        }

        FileStorageDTO dto = new FileStorageDTO();
        dto.setId(fileStorage.getId());
        dto.setFileName(fileStorage.getFileName());
        dto.setFileType(fileStorage.getFileType());
        dto.setFileSize(fileStorage.getFileSize());
        dto.setUploadDate(fileStorage.getUploadDate());

        if (fileStorage.getUploader() != null) {
            dto.setUploaderId(fileStorage.getUploader().getId());
            dto.setUploaderLogin(fileStorage.getUploader().getLogin());
        }

        return dto;
    }
}
