package at.alpscraft.web.rest;

import at.alpscraft.service.PostCategoryService;
import at.alpscraft.service.dto.PostCategoryDTO;
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
 * REST controller for managing {@link at.alpscraft.domain.PostCategory}.
 */
@RestController
@RequestMapping("/api/post-categories")
public class PostCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostCategoryResource.class);

    private static final String ENTITY_NAME = "postCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostCategoryService postCategoryService;

    public PostCategoryResource(PostCategoryService postCategoryService) {
        this.postCategoryService = postCategoryService;
    }

    /**
     * {@code POST  /post-categories} : Create a new postCategory.
     *
     * @param postCategoryDTO the postCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postCategory, or with status {@code 400 (Bad Request)} if the postCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostCategoryDTO> createPostCategory(@Valid @RequestBody PostCategoryDTO postCategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PostCategory : {}", postCategoryDTO);
        if (postCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new postCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostCategoryDTO result = postCategoryService.save(postCategoryDTO);
        return ResponseEntity.created(new URI("/api/post-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-categories/:id} : Updates an existing postCategory.
     *
     * @param id the id of the postCategory to save.
     * @param postCategoryDTO the postCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCategory,
     * or with status {@code 400 (Bad Request)} if the postCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostCategoryDTO> updatePostCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostCategoryDTO postCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PostCategory : {}, {}", id, postCategoryDTO);
        if (postCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postCategoryService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostCategoryDTO result = postCategoryService.update(postCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-categories/:id} : Partial updates given fields of an existing postCategory, field will ignore if it is null
     *
     * @param id the id of the postCategory to save.
     * @param postCategoryDTO the postCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCategory,
     * or with status {@code 400 (Bad Request)} if the postCategory is not valid,
     * or with status {@code 404 (Not Found)} if the postCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the postCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostCategoryDTO> partialUpdatePostCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostCategoryDTO postCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PostCategory partially : {}, {}", id, postCategoryDTO);
        if (postCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postCategoryService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostCategoryDTO> result = postCategoryService.partialUpdate(postCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /post-categories} : get all the postCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postCategories in body.
     */
    @GetMapping("")
    public List<PostCategoryDTO> getAllPostCategories() {
        LOG.debug("REST request to get all PostCategories");
        return postCategoryService.findAll();
    }

    /**
     * {@code GET  /post-categories/:id} : get the "id" postCategory.
     *
     * @param id the id of the postCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostCategoryDTO> getPostCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PostCategory : {}", id);
        Optional<PostCategoryDTO> postCategoryDTO = postCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postCategoryDTO);
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
        postCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
