package at.alpscraft.web.rest;

import static at.alpscraft.domain.UiSectionElementAsserts.*;
import static at.alpscraft.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.alpscraft.IntegrationTest;
import at.alpscraft.domain.UiSectionElement;
import at.alpscraft.domain.enumeration.SectionType;
import at.alpscraft.repository.UiSectionElementRepository;
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
 * Integration tests for the {@link UiSectionElementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UiSectionElementResourceIT {

    private static final SectionType DEFAULT_TITLE = SectionType.HEADER;
    private static final SectionType UPDATED_TITLE = SectionType.MAIN;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ui-section-elements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UiSectionElementRepository uiSectionElementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUiSectionElementMockMvc;

    private UiSectionElement uiSectionElement;

    private UiSectionElement insertedUiSectionElement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UiSectionElement createEntity() {
        return new UiSectionElement().title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UiSectionElement createUpdatedEntity() {
        return new UiSectionElement().title(UPDATED_TITLE).content(UPDATED_CONTENT);
    }

    @BeforeEach
    public void initTest() {
        uiSectionElement = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUiSectionElement != null) {
            uiSectionElementRepository.delete(insertedUiSectionElement);
            insertedUiSectionElement = null;
        }
    }

    @Test
    @Transactional
    void createUiSectionElement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UiSectionElement
        var returnedUiSectionElement = om.readValue(
            restUiSectionElementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSectionElement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UiSectionElement.class
        );

        // Validate the UiSectionElement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUiSectionElementUpdatableFieldsEquals(returnedUiSectionElement, getPersistedUiSectionElement(returnedUiSectionElement));

        insertedUiSectionElement = returnedUiSectionElement;
    }

    @Test
    @Transactional
    void createUiSectionElementWithExistingId() throws Exception {
        // Create the UiSectionElement with an existing ID
        uiSectionElement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUiSectionElementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSectionElement)))
            .andExpect(status().isBadRequest());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uiSectionElement.setTitle(null);

        // Create the UiSectionElement, which fails.

        restUiSectionElementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSectionElement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uiSectionElement.setContent(null);

        // Create the UiSectionElement, which fails.

        restUiSectionElementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSectionElement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUiSectionElements() throws Exception {
        // Initialize the database
        insertedUiSectionElement = uiSectionElementRepository.saveAndFlush(uiSectionElement);

        // Get all the uiSectionElementList
        restUiSectionElementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uiSectionElement.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getUiSectionElement() throws Exception {
        // Initialize the database
        insertedUiSectionElement = uiSectionElementRepository.saveAndFlush(uiSectionElement);

        // Get the uiSectionElement
        restUiSectionElementMockMvc
            .perform(get(ENTITY_API_URL_ID, uiSectionElement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uiSectionElement.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingUiSectionElement() throws Exception {
        // Get the uiSectionElement
        restUiSectionElementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUiSectionElement() throws Exception {
        // Initialize the database
        insertedUiSectionElement = uiSectionElementRepository.saveAndFlush(uiSectionElement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uiSectionElement
        UiSectionElement updatedUiSectionElement = uiSectionElementRepository.findById(uiSectionElement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUiSectionElement are not directly saved in db
        em.detach(updatedUiSectionElement);
        updatedUiSectionElement.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restUiSectionElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUiSectionElement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUiSectionElement))
            )
            .andExpect(status().isOk());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUiSectionElementToMatchAllProperties(updatedUiSectionElement);
    }

    @Test
    @Transactional
    void putNonExistingUiSectionElement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSectionElement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUiSectionElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uiSectionElement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uiSectionElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUiSectionElement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSectionElement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uiSectionElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUiSectionElement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSectionElement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionElementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uiSectionElement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUiSectionElementWithPatch() throws Exception {
        // Initialize the database
        insertedUiSectionElement = uiSectionElementRepository.saveAndFlush(uiSectionElement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uiSectionElement using partial update
        UiSectionElement partialUpdatedUiSectionElement = new UiSectionElement();
        partialUpdatedUiSectionElement.setId(uiSectionElement.getId());

        restUiSectionElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUiSectionElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUiSectionElement))
            )
            .andExpect(status().isOk());

        // Validate the UiSectionElement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUiSectionElementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUiSectionElement, uiSectionElement),
            getPersistedUiSectionElement(uiSectionElement)
        );
    }

    @Test
    @Transactional
    void fullUpdateUiSectionElementWithPatch() throws Exception {
        // Initialize the database
        insertedUiSectionElement = uiSectionElementRepository.saveAndFlush(uiSectionElement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uiSectionElement using partial update
        UiSectionElement partialUpdatedUiSectionElement = new UiSectionElement();
        partialUpdatedUiSectionElement.setId(uiSectionElement.getId());

        partialUpdatedUiSectionElement.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restUiSectionElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUiSectionElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUiSectionElement))
            )
            .andExpect(status().isOk());

        // Validate the UiSectionElement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUiSectionElementUpdatableFieldsEquals(
            partialUpdatedUiSectionElement,
            getPersistedUiSectionElement(partialUpdatedUiSectionElement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUiSectionElement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSectionElement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUiSectionElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uiSectionElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uiSectionElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUiSectionElement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSectionElement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uiSectionElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUiSectionElement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uiSectionElement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUiSectionElementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uiSectionElement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UiSectionElement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUiSectionElement() throws Exception {
        // Initialize the database
        insertedUiSectionElement = uiSectionElementRepository.saveAndFlush(uiSectionElement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uiSectionElement
        restUiSectionElementMockMvc
            .perform(delete(ENTITY_API_URL_ID, uiSectionElement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uiSectionElementRepository.count();
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

    protected UiSectionElement getPersistedUiSectionElement(UiSectionElement uiSectionElement) {
        return uiSectionElementRepository.findById(uiSectionElement.getId()).orElseThrow();
    }

    protected void assertPersistedUiSectionElementToMatchAllProperties(UiSectionElement expectedUiSectionElement) {
        assertUiSectionElementAllPropertiesEquals(expectedUiSectionElement, getPersistedUiSectionElement(expectedUiSectionElement));
    }

    protected void assertPersistedUiSectionElementToMatchUpdatableProperties(UiSectionElement expectedUiSectionElement) {
        assertUiSectionElementAllUpdatablePropertiesEquals(
            expectedUiSectionElement,
            getPersistedUiSectionElement(expectedUiSectionElement)
        );
    }
}
