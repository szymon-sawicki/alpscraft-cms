package at.alpscraft.domain;

import at.alpscraft.domain.enumeration.SectionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UiSection.
 */
@Entity
@Table(name = "ui_section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UiSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "title", nullable = false)
    private SectionType title;

    @Column(name = "css_class")
    private String cssClass;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UiSection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SectionType getTitle() {
        return this.title;
    }

    public UiSection title(SectionType title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(SectionType title) {
        this.title = title;
    }

    public String getCssClass() {
        return this.cssClass;
    }

    public UiSection cssClass(String cssClass) {
        this.setCssClass(cssClass);
        return this;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getContent() {
        return this.content;
    }

    public UiSection content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UiSection)) {
            return false;
        }
        return getId() != null && getId().equals(((UiSection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UiSection{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", cssClass='" + getCssClass() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
