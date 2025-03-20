package at.alpscraft.web.rest;

import at.alpscraft.domain.UiSectionElement;
import at.alpscraft.repository.UiSectionElementRepository;
import at.alpscraft.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.alpscraft.domain.UiSectionElement}.
 */
@RestController
@RequestMapping("/api/ui-section-elements")
@Transactional
public class UiSectionElementResource {

    private static final Logger LOG = LoggerFactory.getLogger(UiSectionElementResource.class);

    private static final String ENTITY_NAME = "uiSectionElement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UiSectionElementRepository uiSectionElementRepository;

    public UiSectionElementResource(UiSectionElementRepository uiSectionElementRepository) {
        this.uiSectionElementRepository = uiSectionElementRepository;
    }

    /**
     * {@code POST  /ui-section-elements} : Create a new uiSectionElement.
     *
     * @param uiSectionElement the uiSectionElement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uiSectionElement, or with status {@code 400 (Bad Request)} if the uiSectionElement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UiSectionElement> createUiSectionElement(@Valid @RequestBody UiSectionElement uiSectionElement)
        throws URISyntaxException {
        LOG.debug("REST request to save UiSectionElement : {}", uiSectionElement);
        if (uiSectionElement.getId() != null) {
            throw new BadRequestAlertException("A new uiSectionElement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uiSectionElement = uiSectionElementRepository.save(uiSectionElement);
        return ResponseEntity.created(new URI("/api/ui-section-elements/" + uiSectionElement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uiSectionElement.getId().toString()))
            .body(uiSectionElement);
    }

    /**
     * {@code PUT  /ui-section-elements/:id} : Updates an existing uiSectionElement.
     *
     * @param id the id of the uiSectionElement to save.
     * @param uiSectionElement the uiSectionElement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uiSectionElement,
     * or with status {@code 400 (Bad Request)} if the uiSectionElement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uiSectionElement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UiSectionElement> updateUiSectionElement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UiSectionElement uiSectionElement
    ) throws URISyntaxException {
        LOG.debug("REST request to update UiSectionElement : {}, {}", id, uiSectionElement);
        if (uiSectionElement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uiSectionElement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uiSectionElementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        uiSectionElement = uiSectionElementRepository.save(uiSectionElement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uiSectionElement.getId().toString()))
            .body(uiSectionElement);
    }

    /**
     * {@code PATCH  /ui-section-elements/:id} : Partial updates given fields of an existing uiSectionElement, field will ignore if it is null
     *
     * @param id the id of the uiSectionElement to save.
     * @param uiSectionElement the uiSectionElement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uiSectionElement,
     * or with status {@code 400 (Bad Request)} if the uiSectionElement is not valid,
     * or with status {@code 404 (Not Found)} if the uiSectionElement is not found,
     * or with status {@code 500 (Internal Server Error)} if the uiSectionElement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UiSectionElement> partialUpdateUiSectionElement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UiSectionElement uiSectionElement
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UiSectionElement partially : {}, {}", id, uiSectionElement);
        if (uiSectionElement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uiSectionElement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uiSectionElementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UiSectionElement> result = uiSectionElementRepository
            .findById(uiSectionElement.getId())
            .map(existingUiSectionElement -> {
                if (uiSectionElement.getTitle() != null) {
                    existingUiSectionElement.setTitle(uiSectionElement.getTitle());
                }
                if (uiSectionElement.getContent() != null) {
                    existingUiSectionElement.setContent(uiSectionElement.getContent());
                }

                return existingUiSectionElement;
            })
            .map(uiSectionElementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uiSectionElement.getId().toString())
        );
    }

    /**
     * {@code GET  /ui-section-elements} : get all the uiSectionElements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uiSectionElements in body.
     */
    @GetMapping("")
    public List<UiSectionElement> getAllUiSectionElements() {
        LOG.debug("REST request to get all UiSectionElements");
        return uiSectionElementRepository.findAll();
    }

    /**
     * {@code GET  /ui-section-elements/:id} : get the "id" uiSectionElement.
     *
     * @param id the id of the uiSectionElement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uiSectionElement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UiSectionElement> getUiSectionElement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UiSectionElement : {}", id);
        Optional<UiSectionElement> uiSectionElement = uiSectionElementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(uiSectionElement);
    }

    /**
     * {@code DELETE  /ui-section-elements/:id} : delete the "id" uiSectionElement.
     *
     * @param id the id of the uiSectionElement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUiSectionElement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UiSectionElement : {}", id);
        uiSectionElementRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
