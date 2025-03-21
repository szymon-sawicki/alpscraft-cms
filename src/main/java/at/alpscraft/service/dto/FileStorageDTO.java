package at.alpscraft.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the FileStorage entity.
 */
public class FileStorageDTO implements Serializable {

    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private Instant uploadDate;
    private Long uploaderId;
    private String uploaderLogin;
    private String fileUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploaderLogin() {
        return uploaderLogin;
    }

    public void setUploaderLogin(String uploaderLogin) {
        this.uploaderLogin = uploaderLogin;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileStorageDTO)) {
            return false;
        }

        FileStorageDTO fileStorageDTO = (FileStorageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileStorageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "FileStorageDTO{" +
            "id=" +
            getId() +
            ", fileName='" +
            getFileName() +
            "'" +
            ", fileType='" +
            getFileType() +
            "'" +
            ", fileSize=" +
            getFileSize() +
            ", uploadDate='" +
            getUploadDate() +
            "'" +
            ", uploaderId=" +
            getUploaderId() +
            ", uploaderLogin='" +
            getUploaderLogin() +
            "'" +
            "}"
        );
    }
}
