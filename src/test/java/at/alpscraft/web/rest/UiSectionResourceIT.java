package at.alpscraft.web.rest;

import static at.alpscraft.domain.UiSectionAsserts.*;
import static at.alpscraft.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.alpscraft.IntegrationTest;
import at.alpscraft.domain.UiSection;
import at.alpscraft.domain.enumeration.SectionType;
import at.alpscraft.repository.UiSectionRepository;
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
 * Integration tests for the {@link UiSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UiSectionResourceIT {

    private static final SectionType DEFAULT_TITLE = SectionType.HEADER;
    private static final SectionType UPDATED_TITLE = SectionType.MAIN;

    private static final String DEFAULT_CSS_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_CSS_CLASS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ui-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UiSectionRepository uiSectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUiSectionMockMvc;

    private UiSection uiSection;

    private UiSection insertedUiSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UiSection createEntity() {
        return new UiSection().title(DEFAULT_TITLE).cssClass(DEFAULT_CSS_CLASS).content(DEFAULT_CONTENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UiSection createUpdatedEntity() {
        return new UiSection().title(UPDATED_TITLE).cssClass(UPDATED_CSS_CLASS).content(UPDATED_CONTENT);
    }

    @BeforeEach
    public void initTest() {
        uiSection = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUiSection != null) {
            uiSectionRepository.delete(insertedUiSection);
            insertedUiSection = null;
        }
    }

    @Test
    @Transactional
    void createUiSection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UiSection
        var returnedUiSection = om.readValue(
            restUiSectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSection)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UiSection.class
        );

        // Validate the UiSection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUiSectionUpdatableFieldsEquals(returnedUiSection, getPersistedUiSection(returnedUiSection));

        insertedUiSection = returnedUiSection;
    }

    @Test
    @Transactional
    void createUiSectionWithExistingId() throws Exception {
        // Create the UiSection with an existing ID
        uiSection.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUiSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSection)))
            .andExpect(status().isBadRequest());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uiSection.setTitle(null);

        // Create the UiSection, which fails.

        restUiSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uiSection.setContent(null);

        // Create the UiSection, which fails.

        restUiSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUiSections() throws Exception {
        // Initialize the database
        insertedUiSection = uiSectionRepository.saveAndFlush(uiSection);

        // Get all the uiSectionList
        restUiSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uiSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].cssClass").value(hasItem(DEFAULT_CSS_CLASS)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getUiSection() throws Exception {
        // Initialize the database
        insertedUiSection = uiSectionRepository.saveAndFlush(uiSection);

        // Get the uiSection
        restUiSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, uiSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uiSection.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.cssClass").value(DEFAULT_CSS_CLASS))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingUiSection() throws Exception {
        // Get the uiSection
        restUiSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUiSection() throws Exception {
        // Initialize the database
        insertedUiSection = uiSectionRepository.saveAndFlush(uiSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uiSection
        UiSection updatedUiSection = uiSectionRepository.findById(uiSection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUiSection are not directly saved in db
        em.detach(updatedUiSection);
        updatedUiSection.title(UPDATED_TITLE).cssClass(UPDATED_CSS_CLASS).content(UPDATED_CONTENT);

        restUiSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUiSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUiSection))
            )
            .andExpect(status().isOk());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUiSectionToMatchAllProperties(updatedUiSection);
    }

    @Test
    @Transactional
    void putNonExistingUiSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUiSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uiSection.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUiSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uiSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUiSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUiSectionWithPatch() throws Exception {
        // Initialize the database
        insertedUiSection = uiSectionRepository.saveAndFlush(uiSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uiSection using partial update
        UiSection partialUpdatedUiSection = new UiSection();
        partialUpdatedUiSection.setId(uiSection.getId());

        partialUpdatedUiSection.title(UPDATED_TITLE);

        restUiSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUiSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUiSection))
            )
            .andExpect(status().isOk());

        // Validate the UiSection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUiSectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUiSection, uiSection),
            getPersistedUiSection(uiSection)
        );
    }

    @Test
    @Transactional
    void fullUpdateUiSectionWithPatch() throws Exception {
        // Initialize the database
        insertedUiSection = uiSectionRepository.saveAndFlush(uiSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uiSection using partial update
        UiSection partialUpdatedUiSection = new UiSection();
        partialUpdatedUiSection.setId(uiSection.getId());

        partialUpdatedUiSection.title(UPDATED_TITLE).cssClass(UPDATED_CSS_CLASS).content(UPDATED_CONTENT);

        restUiSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUiSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUiSection))
            )
            .andExpect(status().isOk());

        // Validate the UiSection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUiSectionUpdatableFieldsEquals(partialUpdatedUiSection, getPersistedUiSection(partialUpdatedUiSection));
    }

    @Test
    @Transactional
    void patchNonExistingUiSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUiSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uiSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uiSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUiSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uiSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUiSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uiSection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UiSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUiSection() throws Exception {
        // Initialize the database
        insertedUiSection = uiSectionRepository.saveAndFlush(uiSection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uiSection
        restUiSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, uiSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uiSectionRepository.count();
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

    protected UiSection getPersistedUiSection(UiSection uiSection) {
        return uiSectionRepository.findById(uiSection.getId()).orElseThrow();
    }

    protected void assertPersistedUiSectionToMatchAllProperties(UiSection expectedUiSection) {
        assertUiSectionAllPropertiesEquals(expectedUiSection, getPersistedUiSection(expectedUiSection));
    }

    protected void assertPersistedUiSectionToMatchUpdatableProperties(UiSection expectedUiSection) {
        assertUiSectionAllUpdatablePropertiesEquals(expectedUiSection, getPersistedUiSection(expectedUiSection));
    }
}
