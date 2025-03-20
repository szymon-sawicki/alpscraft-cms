package at.alpscraft.web.rest;

import static at.alpscraft.domain.StaticPageAsserts.*;
import static at.alpscraft.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.alpscraft.IntegrationTest;
import at.alpscraft.domain.StaticPage;
import at.alpscraft.repository.StaticPageRepository;
import at.alpscraft.repository.UserRepository;
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
 * Integration tests for the {@link StaticPageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StaticPageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/static-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StaticPageRepository staticPageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaticPageMockMvc;

    private StaticPage staticPage;

    private StaticPage insertedStaticPage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaticPage createEntity() {
        return new StaticPage().title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaticPage createUpdatedEntity() {
        return new StaticPage().title(UPDATED_TITLE).content(UPDATED_CONTENT);
    }

    @BeforeEach
    public void initTest() {
        staticPage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStaticPage != null) {
            staticPageRepository.delete(insertedStaticPage);
            insertedStaticPage = null;
        }
    }

    @Test
    @Transactional
    void createStaticPage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StaticPage
        var returnedStaticPage = om.readValue(
            restStaticPageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPage)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StaticPage.class
        );

        // Validate the StaticPage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStaticPageUpdatableFieldsEquals(returnedStaticPage, getPersistedStaticPage(returnedStaticPage));

        insertedStaticPage = returnedStaticPage;
    }

    @Test
    @Transactional
    void createStaticPageWithExistingId() throws Exception {
        // Create the StaticPage with an existing ID
        staticPage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaticPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPage)))
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        staticPage.setTitle(null);

        // Create the StaticPage, which fails.

        restStaticPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        staticPage.setContent(null);

        // Create the StaticPage, which fails.

        restStaticPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPage)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStaticPages() throws Exception {
        // Initialize the database
        insertedStaticPage = staticPageRepository.saveAndFlush(staticPage);

        // Get all the staticPageList
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staticPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getStaticPage() throws Exception {
        // Initialize the database
        insertedStaticPage = staticPageRepository.saveAndFlush(staticPage);

        // Get the staticPage
        restStaticPageMockMvc
            .perform(get(ENTITY_API_URL_ID, staticPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staticPage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingStaticPage() throws Exception {
        // Get the staticPage
        restStaticPageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStaticPage() throws Exception {
        // Initialize the database
        insertedStaticPage = staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staticPage
        StaticPage updatedStaticPage = staticPageRepository.findById(staticPage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStaticPage are not directly saved in db
        em.detach(updatedStaticPage);
        updatedStaticPage.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restStaticPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStaticPage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStaticPage))
            )
            .andExpect(status().isOk());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStaticPageToMatchAllProperties(updatedStaticPage);
    }

    @Test
    @Transactional
    void putNonExistingStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staticPage.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(staticPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(staticPage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaticPageWithPatch() throws Exception {
        // Initialize the database
        insertedStaticPage = staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staticPage using partial update
        StaticPage partialUpdatedStaticPage = new StaticPage();
        partialUpdatedStaticPage.setId(staticPage.getId());

        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaticPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStaticPage))
            )
            .andExpect(status().isOk());

        // Validate the StaticPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStaticPageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStaticPage, staticPage),
            getPersistedStaticPage(staticPage)
        );
    }

    @Test
    @Transactional
    void fullUpdateStaticPageWithPatch() throws Exception {
        // Initialize the database
        insertedStaticPage = staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the staticPage using partial update
        StaticPage partialUpdatedStaticPage = new StaticPage();
        partialUpdatedStaticPage.setId(staticPage.getId());

        partialUpdatedStaticPage.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaticPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStaticPage))
            )
            .andExpect(status().isOk());

        // Validate the StaticPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStaticPageUpdatableFieldsEquals(partialUpdatedStaticPage, getPersistedStaticPage(partialUpdatedStaticPage));
    }

    @Test
    @Transactional
    void patchNonExistingStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staticPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(staticPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(staticPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaticPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        staticPage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticPageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(staticPage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaticPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaticPage() throws Exception {
        // Initialize the database
        insertedStaticPage = staticPageRepository.saveAndFlush(staticPage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the staticPage
        restStaticPageMockMvc
            .perform(delete(ENTITY_API_URL_ID, staticPage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return staticPageRepository.count();
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

    protected StaticPage getPersistedStaticPage(StaticPage staticPage) {
        return staticPageRepository.findById(staticPage.getId()).orElseThrow();
    }

    protected void assertPersistedStaticPageToMatchAllProperties(StaticPage expectedStaticPage) {
        assertStaticPageAllPropertiesEquals(expectedStaticPage, getPersistedStaticPage(expectedStaticPage));
    }

    protected void assertPersistedStaticPageToMatchUpdatableProperties(StaticPage expectedStaticPage) {
        assertStaticPageAllUpdatablePropertiesEquals(expectedStaticPage, getPersistedStaticPage(expectedStaticPage));
    }
}
