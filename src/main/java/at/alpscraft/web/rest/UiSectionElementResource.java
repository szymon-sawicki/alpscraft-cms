package at.alpscraft.web.rest;

import at.alpscraft.service.UiSectionElementService;
import at.alpscraft.service.dto.UiSectionElementDTO;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.alpscraft.domain.UiSectionElement}.
 */
@RestController
@RequestMapping("/api/ui-section-elements")
public class UiSectionElementResource {

    private static final Logger LOG = LoggerFactory.getLogger(UiSectionElementResource.class);

    private static final String ENTITY_NAME = "uiSectionElement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UiSectionElementService uiSectionElementService;

    public UiSectionElementResource(UiSectionElementService uiSectionElementService) {
        this.uiSectionElementService = uiSectionElementService;
    }

    /**
     * {@code POST  /ui-section-elements} : Create a new uiSectionElement.
     *
     * @param uiSectionElementDTO the uiSectionElement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uiSectionElement, or with status {@code 400 (Bad Request)} if the uiSectionElement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UiSectionElementDTO> createUiSectionElement(@Valid @RequestBody UiSectionElementDTO uiSectionElementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UiSectionElement : {}", uiSectionElementDTO);
        if (uiSectionElementDTO.getId() != null) {
            throw new BadRequestAlertException("A new uiSectionElement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UiSectionElementDTO result = uiSectionElementService.save(uiSectionElementDTO);
        return ResponseEntity.created(new URI("/api/ui-section-elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ui-section-elements/:id} : Updates an existing uiSectionElement.
     *
     * @param id the id of the uiSectionElement to save.
     * @param uiSectionElementDTO the uiSectionElement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uiSectionElement,
     * or with status {@code 400 (Bad Request)} if the uiSectionElement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uiSectionElement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UiSectionElementDTO> updateUiSectionElement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UiSectionElementDTO uiSectionElementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UiSectionElement : {}, {}", id, uiSectionElementDTO);
        if (uiSectionElementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uiSectionElementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uiSectionElementService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UiSectionElementDTO result = uiSectionElementService.update(uiSectionElementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uiSectionElementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ui-section-elements/:id} : Partial updates given fields of an existing uiSectionElement, field will ignore if it is null
     *
     * @param id the id of the uiSectionElement to save.
     * @param uiSectionElementDTO the uiSectionElement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uiSectionElement,
     * or with status {@code 400 (Bad Request)} if the uiSectionElement is not valid,
     * or with status {@code 404 (Not Found)} if the uiSectionElement is not found,
     * or with status {@code 500 (Internal Server Error)} if the uiSectionElement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UiSectionElementDTO> partialUpdateUiSectionElement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UiSectionElementDTO uiSectionElementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UiSectionElement partially : {}, {}", id, uiSectionElementDTO);
        if (uiSectionElementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uiSectionElementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uiSectionElementService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UiSectionElementDTO> result = uiSectionElementService.partialUpdate(uiSectionElementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uiSectionElementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ui-section-elements} : get all the uiSectionElements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uiSectionElements in body.
     */
    @GetMapping("")
    public List<UiSectionElementDTO> getAllUiSectionElements() {
        LOG.debug("REST request to get all UiSectionElements");
        return uiSectionElementService.findAll();
    }

    /**
     * {@code GET  /ui-section-elements/:id} : get the "id" uiSectionElement.
     *
     * @param id the id of the uiSectionElement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uiSectionElement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UiSectionElementDTO> getUiSectionElement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UiSectionElement : {}", id);
        Optional<UiSectionElementDTO> uiSectionElementDTO = uiSectionElementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uiSectionElementDTO);
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
        uiSectionElementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
