package at.alpscraft.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link at.alpscraft.domain.BlogPost} entity.
 */
public class BlogPostDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private Long categoryId;
    private String categoryName;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        if (!(o instanceof BlogPostDTO)) {
            return false;
        }

        BlogPostDTO blogPostDTO = (BlogPostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blogPostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "BlogPostDTO{" +
            "id=" +
            getId() +
            ", title='" +
            getTitle() +
            "'" +
            ", content='" +
            getContent() +
            "'" +
            ", categoryId=" +
            getCategoryId() +
            ", categoryName='" +
            getCategoryName() +
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
