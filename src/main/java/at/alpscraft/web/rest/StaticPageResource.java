package at.alpscraft.web.rest;

import at.alpscraft.service.StaticPageService;
import at.alpscraft.service.dto.StaticPageDTO;
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
 * REST controller for managing {@link at.alpscraft.domain.StaticPage}.
 */
@RestController
@RequestMapping("/api/static-pages")
public class StaticPageResource {

    private static final Logger LOG = LoggerFactory.getLogger(StaticPageResource.class);

    private static final String ENTITY_NAME = "staticPage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaticPageService staticPageService;

    public StaticPageResource(StaticPageService staticPageService) {
        this.staticPageService = staticPageService;
    }

    /**
     * {@code POST  /static-pages} : Create a new staticPage.
     *
     * @param staticPageDTO the staticPage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staticPage, or with status {@code 400 (Bad Request)} if the staticPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StaticPageDTO> createStaticPage(@Valid @RequestBody StaticPageDTO staticPageDTO) throws URISyntaxException {
        LOG.debug("REST request to save StaticPage : {}", staticPageDTO);
        if (staticPageDTO.getId() != null) {
            throw new BadRequestAlertException("A new staticPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StaticPageDTO result = staticPageService.save(staticPageDTO);
        return ResponseEntity.created(new URI("/api/static-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /static-pages/:id} : Updates an existing staticPage.
     *
     * @param id the id of the staticPage to save.
     * @param staticPageDTO the staticPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticPage,
     * or with status {@code 400 (Bad Request)} if the staticPage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staticPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StaticPageDTO> updateStaticPage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StaticPageDTO staticPageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StaticPage : {}, {}", id, staticPageDTO);
        if (staticPageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticPageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticPageService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StaticPageDTO result = staticPageService.update(staticPageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticPageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /static-pages/:id} : Partial updates given fields of an existing staticPage, field will ignore if it is null
     *
     * @param id the id of the staticPage to save.
     * @param staticPageDTO the staticPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticPage,
     * or with status {@code 400 (Bad Request)} if the staticPage is not valid,
     * or with status {@code 404 (Not Found)} if the staticPage is not found,
     * or with status {@code 500 (Internal Server Error)} if the staticPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaticPageDTO> partialUpdateStaticPage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StaticPageDTO staticPageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StaticPage partially : {}, {}", id, staticPageDTO);
        if (staticPageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticPageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticPageService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaticPageDTO> result = staticPageService.partialUpdate(staticPageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticPageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /static-pages} : get all the staticPages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staticPages in body.
     */
    @GetMapping("")
    public List<StaticPageDTO> getAllStaticPages() {
        LOG.debug("REST request to get all StaticPages");
        return staticPageService.findAll();
    }

    /**
     * {@code GET  /static-pages/:id} : get the "id" staticPage.
     *
     * @param id the id of the staticPage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staticPage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaticPageDTO> getStaticPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StaticPage : {}", id);
        Optional<StaticPageDTO> staticPageDTO = staticPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(staticPageDTO);
    }

    /**
     * {@code DELETE  /static-pages/:id} : delete the "id" staticPage.
     *
     * @param id the id of the staticPage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaticPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StaticPage : {}", id);
        staticPageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
