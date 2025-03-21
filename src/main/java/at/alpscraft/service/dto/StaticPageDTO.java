package at.alpscraft.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link at.alpscraft.domain.StaticPage} entity.
 */
public class StaticPageDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private Long authorId;
    private String authorUsername;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaticPageDTO)) {
            return false;
        }

        StaticPageDTO staticPageDTO = (StaticPageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staticPageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "StaticPageDTO{" +
            "id=" +
            getId() +
            ", title='" +
            getTitle() +
            "'" +
            ", content='" +
            getContent() +
            "'" +
            ", authorId=" +
            getAuthorId() +
            ", authorUsername='" +
            getAuthorUsername() +
            "'" +
            "}"
        );
    }
}
