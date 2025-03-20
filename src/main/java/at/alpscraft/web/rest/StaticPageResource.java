package at.alpscraft.web.rest;

import at.alpscraft.domain.StaticPage;
import at.alpscraft.repository.StaticPageRepository;
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
 * REST controller for managing {@link at.alpscraft.domain.StaticPage}.
 */
@RestController
@RequestMapping("/api/static-pages")
@Transactional
public class StaticPageResource {

    private static final Logger LOG = LoggerFactory.getLogger(StaticPageResource.class);

    private static final String ENTITY_NAME = "staticPage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaticPageRepository staticPageRepository;

    public StaticPageResource(StaticPageRepository staticPageRepository) {
        this.staticPageRepository = staticPageRepository;
    }

    /**
     * {@code POST  /static-pages} : Create a new staticPage.
     *
     * @param staticPage the staticPage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staticPage, or with status {@code 400 (Bad Request)} if the staticPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StaticPage> createStaticPage(@Valid @RequestBody StaticPage staticPage) throws URISyntaxException {
        LOG.debug("REST request to save StaticPage : {}", staticPage);
        if (staticPage.getId() != null) {
            throw new BadRequestAlertException("A new staticPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        staticPage = staticPageRepository.save(staticPage);
        return ResponseEntity.created(new URI("/api/static-pages/" + staticPage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, staticPage.getId().toString()))
            .body(staticPage);
    }

    /**
     * {@code PUT  /static-pages/:id} : Updates an existing staticPage.
     *
     * @param id the id of the staticPage to save.
     * @param staticPage the staticPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticPage,
     * or with status {@code 400 (Bad Request)} if the staticPage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staticPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StaticPage> updateStaticPage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StaticPage staticPage
    ) throws URISyntaxException {
        LOG.debug("REST request to update StaticPage : {}, {}", id, staticPage);
        if (staticPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        staticPage = staticPageRepository.save(staticPage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticPage.getId().toString()))
            .body(staticPage);
    }

    /**
     * {@code PATCH  /static-pages/:id} : Partial updates given fields of an existing staticPage, field will ignore if it is null
     *
     * @param id the id of the staticPage to save.
     * @param staticPage the staticPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticPage,
     * or with status {@code 400 (Bad Request)} if the staticPage is not valid,
     * or with status {@code 404 (Not Found)} if the staticPage is not found,
     * or with status {@code 500 (Internal Server Error)} if the staticPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaticPage> partialUpdateStaticPage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StaticPage staticPage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StaticPage partially : {}, {}", id, staticPage);
        if (staticPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaticPage> result = staticPageRepository
            .findById(staticPage.getId())
            .map(existingStaticPage -> {
                if (staticPage.getTitle() != null) {
                    existingStaticPage.setTitle(staticPage.getTitle());
                }
                if (staticPage.getContent() != null) {
                    existingStaticPage.setContent(staticPage.getContent());
                }

                return existingStaticPage;
            })
            .map(staticPageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticPage.getId().toString())
        );
    }

    /**
     * {@code GET  /static-pages} : get all the staticPages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staticPages in body.
     */
    @GetMapping("")
    public List<StaticPage> getAllStaticPages() {
        LOG.debug("REST request to get all StaticPages");
        return staticPageRepository.findAll();
    }

    /**
     * {@code GET  /static-pages/:id} : get the "id" staticPage.
     *
     * @param id the id of the staticPage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staticPage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaticPage> getStaticPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StaticPage : {}", id);
        Optional<StaticPage> staticPage = staticPageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(staticPage);
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
        staticPageRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
