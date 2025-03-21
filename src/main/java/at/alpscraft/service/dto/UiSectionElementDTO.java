package at.alpscraft.service.dto;

import at.alpscraft.domain.enumeration.SectionType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link at.alpscraft.domain.UiSectionElement} entity.
 */
public class UiSectionElementDTO implements Serializable {

    private Long id;

    @NotNull
    private SectionType title;

    @NotNull
    private String content;

    private Long uiSectionId;
    private SectionType uiSectionTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SectionType getTitle() {
        return title;
    }

    public void setTitle(SectionType title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUiSectionId() {
        return uiSectionId;
    }

    public void setUiSectionId(Long uiSectionId) {
        this.uiSectionId = uiSectionId;
    }

    public SectionType getUiSectionTitle() {
        return uiSectionTitle;
    }

    public void setUiSectionTitle(SectionType uiSectionTitle) {
        this.uiSectionTitle = uiSectionTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UiSectionElementDTO)) {
            return false;
        }

        UiSectionElementDTO uiSectionElementDTO = (UiSectionElementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uiSectionElementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "UiSectionElementDTO{" +
            "id=" +
            getId() +
            ", title='" +
            getTitle() +
            "'" +
            ", content='" +
            getContent() +
            "'" +
            ", uiSectionId=" +
            getUiSectionId() +
            ", uiSectionTitle='" +
            getUiSectionTitle() +
            "'" +
            "}"
        );
    }
}
