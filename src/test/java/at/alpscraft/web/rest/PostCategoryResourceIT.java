package at.alpscraft.web.rest;

import static at.alpscraft.domain.PostCategoryAsserts.*;
import static at.alpscraft.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.alpscraft.IntegrationTest;
import at.alpscraft.domain.PostCategory;
import at.alpscraft.repository.PostCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PostCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/post-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostCategoryMockMvc;

    private PostCategory postCategory;

    private PostCategory insertedPostCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostCategory createEntity() {
        return new PostCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostCategory createUpdatedEntity() {
        return new PostCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        postCategory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPostCategory != null) {
            postCategoryRepository.delete(insertedPostCategory);
            insertedPostCategory = null;
        }
    }

    @Test
    @Transactional
    void createPostCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostCategory
        var returnedPostCategory = om.readValue(
            restPostCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostCategory.class
        );

        // Validate the PostCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPostCategoryUpdatableFieldsEquals(returnedPostCategory, getPersistedPostCategory(returnedPostCategory));

        insertedPostCategory = returnedPostCategory;
    }

    @Test
    @Transactional
    void createPostCategoryWithExistingId() throws Exception {
        // Create the PostCategory with an existing ID
        postCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCategory)))
            .andExpect(status().isBadRequest());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postCategory.setName(null);

        // Create the PostCategory, which fails.

        restPostCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCategory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostCategories() throws Exception {
        // Initialize the database
        insertedPostCategory = postCategoryRepository.saveAndFlush(postCategory);

        // Get all the postCategoryList
        restPostCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPostCategory() throws Exception {
        // Initialize the database
        insertedPostCategory = postCategoryRepository.saveAndFlush(postCategory);

        // Get the postCategory
        restPostCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, postCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPostCategory() throws Exception {
        // Get the postCategory
        restPostCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostCategory() throws Exception {
        // Initialize the database
        insertedPostCategory = postCategoryRepository.saveAndFlush(postCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postCategory
        PostCategory updatedPostCategory = postCategoryRepository.findById(postCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostCategory are not directly saved in db
        em.detach(updatedPostCategory);
        updatedPostCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPostCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPostCategory))
            )
            .andExpect(status().isOk());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostCategoryToMatchAllProperties(updatedPostCategory);
    }

    @Test
    @Transactional
    void putNonExistingPostCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedPostCategory = postCategoryRepository.saveAndFlush(postCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postCategory using partial update
        PostCategory partialUpdatedPostCategory = new PostCategory();
        partialUpdatedPostCategory.setId(postCategory.getId());

        restPostCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostCategory))
            )
            .andExpect(status().isOk());

        // Validate the PostCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPostCategory, postCategory),
            getPersistedPostCategory(postCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdatePostCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedPostCategory = postCategoryRepository.saveAndFlush(postCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postCategory using partial update
        PostCategory partialUpdatedPostCategory = new PostCategory();
        partialUpdatedPostCategory.setId(postCategory.getId());

        partialUpdatedPostCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPostCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostCategory))
            )
            .andExpect(status().isOk());

        // Validate the PostCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostCategoryUpdatableFieldsEquals(partialUpdatedPostCategory, getPersistedPostCategory(partialUpdatedPostCategory));
    }

    @Test
    @Transactional
    void patchNonExistingPostCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostCategory() throws Exception {
        // Initialize the database
        insertedPostCategory = postCategoryRepository.saveAndFlush(postCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postCategory
        restPostCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, postCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postCategoryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PostCategory getPersistedPostCategory(PostCategory postCategory) {
        return postCategoryRepository.findById(postCategory.getId()).orElseThrow();
    }

    protected void assertPersistedPostCategoryToMatchAllProperties(PostCategory expectedPostCategory) {
        assertPostCategoryAllPropertiesEquals(expectedPostCategory, getPersistedPostCategory(expectedPostCategory));
    }

    protected void assertPersistedPostCategoryToMatchUpdatableProperties(PostCategory expectedPostCategory) {
        assertPostCategoryAllUpdatablePropertiesEquals(expectedPostCategory, getPersistedPostCategory(expectedPostCategory));
    }
}
