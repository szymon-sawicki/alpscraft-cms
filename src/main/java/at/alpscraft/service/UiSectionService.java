package at.alpscraft.service;

import at.alpscraft.domain.UiSection;
import at.alpscraft.repository.UiSectionRepository;
import at.alpscraft.service.dto.UiSectionDTO;
import at.alpscraft.service.mapper.UiSectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UiSection}.
 */
@Service
@Transactional
public class UiSectionService {

    private final Logger log = LoggerFactory.getLogger(UiSectionService.class);

    private final UiSectionRepository uiSectionRepository;

    private final UiSectionMapper uiSectionMapper;

    public UiSectionService(UiSectionRepository uiSectionRepository, UiSectionMapper uiSectionMapper) {
        this.uiSectionRepository = uiSectionRepository;
        this.uiSectionMapper = uiSectionMapper;
    }

    /**
     * Save a uiSection.
     *
     * @param uiSectionDTO the entity to save.
     * @return the persisted entity.
     */
    public UiSectionDTO save(UiSectionDTO uiSectionDTO) {
        log.debug("Request to save UiSection : {}", uiSectionDTO);
        UiSection uiSection = uiSectionMapper.toEntity(uiSectionDTO);
        uiSection = uiSectionRepository.save(uiSection);
        return uiSectionMapper.toDto(uiSection);
    }

    /**
     * Update a uiSection.
     *
     * @param uiSectionDTO the entity to save.
     * @return the persisted entity.
     */
    public UiSectionDTO update(UiSectionDTO uiSectionDTO) {
        log.debug("Request to update UiSection : {}", uiSectionDTO);
        UiSection uiSection = uiSectionMapper.toEntity(uiSectionDTO);
        uiSection = uiSectionRepository.save(uiSection);
        return uiSectionMapper.toDto(uiSection);
    }

    /**
     * Partially update a uiSection.
     *
     * @param uiSectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UiSectionDTO> partialUpdate(UiSectionDTO uiSectionDTO) {
        log.debug("Request to partially update UiSection : {}", uiSectionDTO);

        return uiSectionRepository
            .findById(uiSectionDTO.getId())
            .map(existingUiSection -> {
                uiSectionMapper.partialUpdate(existingUiSection, uiSectionDTO);
                return existingUiSection;
            })
            .map(uiSectionRepository::save)
            .map(uiSectionMapper::toDto);
    }

    /**
     * Get all the uiSections.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UiSectionDTO> findAll() {
        log.debug("Request to get all UiSections");
        return uiSectionRepository.findAll().stream().map(uiSectionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one uiSection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UiSectionDTO> findOne(Long id) {
        log.debug("Request to get UiSection : {}", id);
        return uiSectionRepository.findById(id).map(uiSectionMapper::toDto);
    }

    /**
     * Delete the uiSection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UiSection : {}", id);
        uiSectionRepository.deleteById(id);
    }
}
