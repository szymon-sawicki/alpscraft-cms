package at.alpscraft.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link at.alpscraft.domain.PostCategory} entity.
 */
public class PostCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostCategoryDTO)) {
            return false;
        }

        PostCategoryDTO postCategoryDTO = (PostCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "PostCategoryDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='" + getDescription() + "'" + "}";
    }
}
