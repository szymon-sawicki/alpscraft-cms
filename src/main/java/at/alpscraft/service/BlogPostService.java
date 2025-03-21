package at.alpscraft.service;

import at.alpscraft.domain.BlogPost;
import at.alpscraft.repository.BlogPostRepository;
import at.alpscraft.service.dto.BlogPostDTO;
import at.alpscraft.service.mapper.BlogPostMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BlogPost}.
 */
@Service
@Transactional
public class BlogPostService {

    private final Logger log = LoggerFactory.getLogger(BlogPostService.class);

    private final BlogPostRepository blogPostRepository;

    private final BlogPostMapper blogPostMapper;

    public BlogPostService(BlogPostRepository blogPostRepository, BlogPostMapper blogPostMapper) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
    }

    /**
     * Save a blogPost.
     *
     * @param blogPostDTO the entity to save.
     * @return the persisted entity.
     */
    public BlogPostDTO save(BlogPostDTO blogPostDTO) {
        log.debug("Request to save BlogPost : {}", blogPostDTO);
        BlogPost blogPost = blogPostMapper.toEntity(blogPostDTO);
        blogPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toDto(blogPost);
    }

    /**
     * Update a blogPost.
     *
     * @param blogPostDTO the entity to save.
     * @return the persisted entity.
     */
    public BlogPostDTO update(BlogPostDTO blogPostDTO) {
        log.debug("Request to update BlogPost : {}", blogPostDTO);
        BlogPost blogPost = blogPostMapper.toEntity(blogPostDTO);
        blogPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toDto(blogPost);
    }

    /**
     * Partially update a blogPost.
     *
     * @param blogPostDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BlogPostDTO> partialUpdate(BlogPostDTO blogPostDTO) {
        log.debug("Request to partially update BlogPost : {}", blogPostDTO);

        return blogPostRepository
            .findById(blogPostDTO.getId())
            .map(existingBlogPost -> {
                blogPostMapper.partialUpdate(existingBlogPost, blogPostDTO);
                return existingBlogPost;
            })
            .map(blogPostRepository::save)
            .map(blogPostMapper::toDto);
    }

    /**
     * Get all the blogPosts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BlogPostDTO> findAll() {
        log.debug("Request to get all BlogPosts");
        return blogPostRepository.findAll().stream().map(blogPostMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the blogPosts with pagination.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BlogPostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BlogPosts with pagination");
        return blogPostRepository.findAll(pageable).map(blogPostMapper::toDto);
    }

    /**
     * Get one blogPost by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BlogPostDTO> findOne(Long id) {
        log.debug("Request to get BlogPost : {}", id);
        return blogPostRepository.findById(id).map(blogPostMapper::toDto);
    }

    /**
     * Delete the blogPost by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BlogPost : {}", id);
        blogPostRepository.deleteById(id);
    }
}
