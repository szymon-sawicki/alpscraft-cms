package at.alpscraft.web.rest;

import at.alpscraft.domain.PostCategory;
import at.alpscraft.repository.PostCategoryRepository;
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
 * REST controller for managing {@link at.alpscraft.domain.PostCategory}.
 */
@RestController
@RequestMapping("/api/post-categories")
@Transactional
public class PostCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostCategoryResource.class);

    private static final String ENTITY_NAME = "postCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostCategoryRepository postCategoryRepository;

    public PostCategoryResource(PostCategoryRepository postCategoryRepository) {
        this.postCategoryRepository = postCategoryRepository;
    }

    /**
     * {@code POST  /post-categories} : Create a new postCategory.
     *
     * @param postCategory the postCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postCategory, or with status {@code 400 (Bad Request)} if the postCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostCategory> createPostCategory(@Valid @RequestBody PostCategory postCategory) throws URISyntaxException {
        LOG.debug("REST request to save PostCategory : {}", postCategory);
        if (postCategory.getId() != null) {
            throw new BadRequestAlertException("A new postCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postCategory = postCategoryRepository.save(postCategory);
        return ResponseEntity.created(new URI("/api/post-categories/" + postCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, postCategory.getId().toString()))
            .body(postCategory);
    }

    /**
     * {@code PUT  /post-categories/:id} : Updates an existing postCategory.
     *
     * @param id the id of the postCategory to save.
     * @param postCategory the postCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCategory,
     * or with status {@code 400 (Bad Request)} if the postCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostCategory> updatePostCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostCategory postCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update PostCategory : {}, {}", id, postCategory);
        if (postCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postCategory = postCategoryRepository.save(postCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postCategory.getId().toString()))
            .body(postCategory);
    }

    /**
     * {@code PATCH  /post-categories/:id} : Partial updates given fields of an existing postCategory, field will ignore if it is null
     *
     * @param id the id of the postCategory to save.
     * @param postCategory the postCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCategory,
     * or with status {@code 400 (Bad Request)} if the postCategory is not valid,
     * or with status {@code 404 (Not Found)} if the postCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the postCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostCategory> partialUpdatePostCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostCategory postCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PostCategory partially : {}, {}", id, postCategory);
        if (postCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostCategory> result = postCategoryRepository
            .findById(postCategory.getId())
            .map(existingPostCategory -> {
                if (postCategory.getName() != null) {
                    existingPostCategory.setName(postCategory.getName());
                }
                if (postCategory.getDescription() != null) {
                    existingPostCategory.setDescription(postCategory.getDescription());
                }

                return existingPostCategory;
            })
            .map(postCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /post-categories} : get all the postCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postCategories in body.
     */
    @GetMapping("")
    public List<PostCategory> getAllPostCategories() {
        LOG.debug("REST request to get all PostCategories");
        return postCategoryRepository.findAll();
    }

    /**
     * {@code GET  /post-categories/:id} : get the "id" postCategory.
     *
     * @param id the id of the postCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostCategory> getPostCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PostCategory : {}", id);
        Optional<PostCategory> postCategory = postCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(postCategory);
    }

    /**
     * {@code DELETE  /post-categories/:id} : delete the "id" postCategory.
     *
     * @param id the id of the postCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PostCategory : {}", id);
        postCategoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
