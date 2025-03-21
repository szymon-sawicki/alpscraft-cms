package at.alpscraft.service.dto;

import at.alpscraft.domain.enumeration.SectionType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link at.alpscraft.domain.UiSection} entity.
 */
public class UiSectionDTO implements Serializable {

    private Long id;

    @NotNull
    private SectionType title;

    private String cssClass;

    @NotNull
    private String content;

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

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UiSectionDTO)) {
            return false;
        }

        UiSectionDTO uiSectionDTO = (UiSectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uiSectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "UiSectionDTO{" +
            "id=" +
            getId() +
            ", title='" +
            getTitle() +
            "'" +
            ", cssClass='" +
            getCssClass() +
            "'" +
            ", content='" +
            getContent() +
            "'" +
            "}"
        );
    }
}
