package at.alpscraft.web.rest;

import at.alpscraft.domain.FileStorage;
import at.alpscraft.service.FileStorageService;
import at.alpscraft.service.dto.FileStorageDTO;
import at.alpscraft.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.alpscraft.domain.FileStorage}.
 */
@RestController
@RequestMapping("/api")
public class FileStorageResource {

    private final Logger log = LoggerFactory.getLogger(FileStorageResource.class);
    private static final String ENTITY_NAME = "fileStorage";
    private static final Pattern BASE64_IMAGE_PATTERN = Pattern.compile("data:([\\w/]+);base64,(.+)");

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileStorageService fileStorageService;

    public FileStorageResource(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * {@code POST /files} : Upload a file.
     *
     * @param file the file to upload
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileStorageDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/files")
    public ResponseEntity<FileStorageDTO> uploadFile(@RequestParam("file") MultipartFile file) throws URISyntaxException {
        log.debug("REST request to upload a file");
        if (file.isEmpty()) {
            throw new BadRequestAlertException("File cannot be empty", ENTITY_NAME, "fileempty");
        }

        FileStorage fileStorage = fileStorageService.storeFile(file);
        FileStorageDTO result = fileStorageService.toDto(fileStorage);

        // Set the file URL
        result.setFileUrl(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/files/").path(fileStorage.getId().toString()).toUriString()
        );

        return ResponseEntity.created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST /files/upload-base64} : Upload a base64 encoded file.
     *
     * @param requestBody the base64 encoded content
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileStorageDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/files/upload-base64")
    public ResponseEntity<FileStorageDTO> uploadBase64File(@RequestBody Map<String, String> requestBody) throws URISyntaxException {
        log.debug("REST request to upload a base64 file");
        String base64Content = requestBody.get("content");
        if (base64Content == null || base64Content.isEmpty()) {
            throw new BadRequestAlertException("File content cannot be empty", ENTITY_NAME, "fileempty");
        }

        Matcher matcher = BASE64_IMAGE_PATTERN.matcher(base64Content);
        String fileType = "application/octet-stream";
        if (matcher.find()) {
            fileType = matcher.group(1);
        }

        String fileName = requestBody.getOrDefault("fileName", "image." + getExtensionFromMimeType(fileType));

        FileStorage fileStorage = fileStorageService.storeBase64File(base64Content, fileName, fileType);
        FileStorageDTO result = fileStorageService.toDto(fileStorage);

        // Set the file URL
        result.setFileUrl(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/files/").path(fileStorage.getId().toString()).toUriString()
        );

        return ResponseEntity.created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET /files} : get all the files metadata.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of files in body
     */
    @GetMapping("/files")
    public List<FileStorageDTO> getAllFiles() {
        log.debug("REST request to get all Files");
        return fileStorageService
            .getFile(null)
            .stream()
            .map(fileStorage -> {
                FileStorageDTO dto = fileStorageService.toDto(fileStorage);
                dto.setFileUrl(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/")
                        .path(fileStorage.getId().toString())
                        .toUriString()
                );
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * {@code GET /files/:id} : get the file content by id.
     *
     * @param id the id of the file to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the file content
     */
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        log.debug("REST request to get File : {}", id);
        Optional<FileStorage> fileStorage = fileStorageService.getFile(id);

        return fileStorage
            .map(fs -> {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(fs.getFileType()));
                headers.setContentDispositionFormData("attachment", fs.getFileName());
                return new ResponseEntity<>(fs.getData(), headers, HttpStatus.OK);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE /files/:id} : delete the file by id.
     *
     * @param id the id of the file to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}
     */
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        log.debug("REST request to delete File : {}", id);
        fileStorageService.deleteFile(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * Get file extension from MIME type
     */
    private String getExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
            case "image/jpg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            case "image/svg+xml":
                return "svg";
            case "image/webp":
                return "webp";
            default:
                return "bin";
        }
    }
}
