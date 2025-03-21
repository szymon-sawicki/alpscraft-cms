package at.alpscraft.web.rest;

import at.alpscraft.service.BlogPostService;
import at.alpscraft.service.dto.BlogPostDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link at.alpscraft.domain.BlogPost}.
 */
@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostResource {

    private static final Logger LOG = LoggerFactory.getLogger(BlogPostResource.class);

    private static final String ENTITY_NAME = "blogPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlogPostService blogPostService;

    public BlogPostResource(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    /**
     * {@code POST  /blog-posts} : Create a new blogPost.
     *
     * @param blogPostDTO the blogPost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blogPost, or with status {@code 400 (Bad Request)} if the blogPost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BlogPostDTO> createBlogPost(@Valid @RequestBody BlogPostDTO blogPostDTO) throws URISyntaxException {
        LOG.debug("REST request to save BlogPost : {}", blogPostDTO);
        if (blogPostDTO.getId() != null) {
            throw new BadRequestAlertException("A new blogPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlogPostDTO result = blogPostService.save(blogPostDTO);
        return ResponseEntity.created(new URI("/api/blog-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /blog-posts/:id} : Updates an existing blogPost.
     *
     * @param id the id of the blogPost to save.
     * @param blogPostDTO the blogPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogPost,
     * or with status {@code 400 (Bad Request)} if the blogPost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blogPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDTO> updateBlogPost(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BlogPostDTO blogPostDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BlogPost : {}, {}", id, blogPostDTO);
        if (blogPostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogPostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogPostService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BlogPostDTO result = blogPostService.update(blogPostDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogPostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /blog-posts/:id} : Partial updates given fields of an existing blogPost, field will ignore if it is null
     *
     * @param id the id of the blogPost to save.
     * @param blogPostDTO the blogPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogPost,
     * or with status {@code 400 (Bad Request)} if the blogPost is not valid,
     * or with status {@code 404 (Not Found)} if the blogPost is not found,
     * or with status {@code 500 (Internal Server Error)} if the blogPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BlogPostDTO> partialUpdateBlogPost(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BlogPostDTO blogPostDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BlogPost partially : {}, {}", id, blogPostDTO);
        if (blogPostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogPostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogPostService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlogPostDTO> result = blogPostService.partialUpdate(blogPostDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogPostDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /blog-posts} : get all the blogPosts.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blogPosts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BlogPostDTO>> getAllBlogPosts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get all BlogPosts");
        Page<BlogPostDTO> page = blogPostService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blog-posts/:id} : get the "id" blogPost.
     *
     * @param id the id of the blogPost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blogPost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDTO> getBlogPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BlogPost : {}", id);
        Optional<BlogPostDTO> blogPostDTO = blogPostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blogPostDTO);
    }

    /**
     * {@code DELETE  /blog-posts/:id} : delete the "id" blogPost.
     *
     * @param id the id of the blogPost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BlogPost : {}", id);
        blogPostService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
