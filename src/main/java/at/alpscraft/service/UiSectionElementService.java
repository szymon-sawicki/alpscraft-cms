package at.alpscraft.service;

import at.alpscraft.domain.UiSectionElement;
import at.alpscraft.repository.UiSectionElementRepository;
import at.alpscraft.service.dto.UiSectionElementDTO;
import at.alpscraft.service.mapper.UiSectionElementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UiSectionElement}.
 */
@Service
@Transactional
public class UiSectionElementService {

    private final Logger log = LoggerFactory.getLogger(UiSectionElementService.class);

    private final UiSectionElementRepository uiSectionElementRepository;

    private final UiSectionElementMapper uiSectionElementMapper;

    public UiSectionElementService(UiSectionElementRepository uiSectionElementRepository, UiSectionElementMapper uiSectionElementMapper) {
        this.uiSectionElementRepository = uiSectionElementRepository;
        this.uiSectionElementMapper = uiSectionElementMapper;
    }

    /**
     * Save a uiSectionElement.
     *
     * @param uiSectionElementDTO the entity to save.
     * @return the persisted entity.
     */
    public UiSectionElementDTO save(UiSectionElementDTO uiSectionElementDTO) {
        log.debug("Request to save UiSectionElement : {}", uiSectionElementDTO);
        UiSectionElement uiSectionElement = uiSectionElementMapper.toEntity(uiSectionElementDTO);
        uiSectionElement = uiSectionElementRepository.save(uiSectionElement);
        return uiSectionElementMapper.toDto(uiSectionElement);
    }

    /**
     * Update a uiSectionElement.
     *
     * @param uiSectionElementDTO the entity to save.
     * @return the persisted entity.
     */
    public UiSectionElementDTO update(UiSectionElementDTO uiSectionElementDTO) {
        log.debug("Request to update UiSectionElement : {}", uiSectionElementDTO);
        UiSectionElement uiSectionElement = uiSectionElementMapper.toEntity(uiSectionElementDTO);
        uiSectionElement = uiSectionElementRepository.save(uiSectionElement);
        return uiSectionElementMapper.toDto(uiSectionElement);
    }

    /**
     * Partially update a uiSectionElement.
     *
     * @param uiSectionElementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UiSectionElementDTO> partialUpdate(UiSectionElementDTO uiSectionElementDTO) {
        log.debug("Request to partially update UiSectionElement : {}", uiSectionElementDTO);

        return uiSectionElementRepository
            .findById(uiSectionElementDTO.getId())
            .map(existingUiSectionElement -> {
                uiSectionElementMapper.partialUpdate(existingUiSectionElement, uiSectionElementDTO);
                return existingUiSectionElement;
            })
            .map(uiSectionElementRepository::save)
            .map(uiSectionElementMapper::toDto);
    }

    /**
     * Get all the uiSectionElements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UiSectionElementDTO> findAll() {
        log.debug("Request to get all UiSectionElements");
        return uiSectionElementRepository
            .findAll()
            .stream()
            .map(uiSectionElementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one uiSectionElement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UiSectionElementDTO> findOne(Long id) {
        log.debug("Request to get UiSectionElement : {}", id);
        return uiSectionElementRepository.findById(id).map(uiSectionElementMapper::toDto);
    }

    /**
     * Delete the uiSectionElement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UiSectionElement : {}", id);
        uiSectionElementRepository.deleteById(id);
    }
}
