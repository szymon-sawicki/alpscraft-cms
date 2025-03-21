package at.alpscraft.web.rest;

import at.alpscraft.service.UiSectionService;
import at.alpscraft.service.dto.UiSectionDTO;
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
 * REST controller for managing {@link at.alpscraft.domain.UiSection}.
 */
@RestController
@RequestMapping("/api/ui-sections")
public class UiSectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UiSectionResource.class);

    private static final String ENTITY_NAME = "uiSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UiSectionService uiSectionService;

    public UiSectionResource(UiSectionService uiSectionService) {
        this.uiSectionService = uiSectionService;
    }

    /**
     * {@code POST  /ui-sections} : Create a new uiSection.
     *
     * @param uiSectionDTO the uiSection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uiSection, or with status {@code 400 (Bad Request)} if the uiSection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UiSectionDTO> createUiSection(@Valid @RequestBody UiSectionDTO uiSectionDTO) throws URISyntaxException {
        LOG.debug("REST request to save UiSection : {}", uiSectionDTO);
        if (uiSectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new uiSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UiSectionDTO result = uiSectionService.save(uiSectionDTO);
        return ResponseEntity.created(new URI("/api/ui-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ui-sections/:id} : Updates an existing uiSection.
     *
     * @param id the id of the uiSection to save.
     * @param uiSectionDTO the uiSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uiSection,
     * or with status {@code 400 (Bad Request)} if the uiSection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uiSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UiSectionDTO> updateUiSection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UiSectionDTO uiSectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UiSection : {}, {}", id, uiSectionDTO);
        if (uiSectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uiSectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uiSectionService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UiSectionDTO result = uiSectionService.update(uiSectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uiSectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ui-sections/:id} : Partial updates given fields of an existing uiSection, field will ignore if it is null
     *
     * @param id the id of the uiSection to save.
     * @param uiSectionDTO the uiSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uiSection,
     * or with status {@code 400 (Bad Request)} if the uiSection is not valid,
     * or with status {@code 404 (Not Found)} if the uiSection is not found,
     * or with status {@code 500 (Internal Server Error)} if the uiSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UiSectionDTO> partialUpdateUiSection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UiSectionDTO uiSectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UiSection partially : {}, {}", id, uiSectionDTO);
        if (uiSectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uiSectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uiSectionService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UiSectionDTO> result = uiSectionService.partialUpdate(uiSectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uiSectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ui-sections} : get all the uiSections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uiSections in body.
     */
    @GetMapping("")
    public List<UiSectionDTO> getAllUiSections() {
        LOG.debug("REST request to get all UiSections");
        return uiSectionService.findAll();
    }

    /**
     * {@code GET  /ui-sections/:id} : get the "id" uiSection.
     *
     * @param id the id of the uiSection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uiSection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UiSectionDTO> getUiSection(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UiSection : {}", id);
        Optional<UiSectionDTO> uiSectionDTO = uiSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uiSectionDTO);
    }

    /**
     * {@code DELETE  /ui-sections/:id} : delete the "id" uiSection.
     *
     * @param id the id of the uiSection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUiSection(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UiSection : {}", id);
        uiSectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
