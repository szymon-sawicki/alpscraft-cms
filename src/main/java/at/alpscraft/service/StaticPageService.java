package at.alpscraft.service;

import at.alpscraft.domain.StaticPage;
import at.alpscraft.repository.StaticPageRepository;
import at.alpscraft.service.dto.StaticPageDTO;
import at.alpscraft.service.mapper.StaticPageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StaticPage}.
 */
@Service
@Transactional
public class StaticPageService {

    private final Logger log = LoggerFactory.getLogger(StaticPageService.class);

    private final StaticPageRepository staticPageRepository;

    private final StaticPageMapper staticPageMapper;

    public StaticPageService(StaticPageRepository staticPageRepository, StaticPageMapper staticPageMapper) {
        this.staticPageRepository = staticPageRepository;
        this.staticPageMapper = staticPageMapper;
    }

    /**
     * Save a staticPage.
     *
     * @param staticPageDTO the entity to save.
     * @return the persisted entity.
     */
    public StaticPageDTO save(StaticPageDTO staticPageDTO) {
        log.debug("Request to save StaticPage : {}", staticPageDTO);
        StaticPage staticPage = staticPageMapper.toEntity(staticPageDTO);
        staticPage = staticPageRepository.save(staticPage);
        return staticPageMapper.toDto(staticPage);
    }

    /**
     * Update a staticPage.
     *
     * @param staticPageDTO the entity to save.
     * @return the persisted entity.
     */
    public StaticPageDTO update(StaticPageDTO staticPageDTO) {
        log.debug("Request to update StaticPage : {}", staticPageDTO);
        StaticPage staticPage = staticPageMapper.toEntity(staticPageDTO);
        staticPage = staticPageRepository.save(staticPage);
        return staticPageMapper.toDto(staticPage);
    }

    /**
     * Partially update a staticPage.
     *
     * @param staticPageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StaticPageDTO> partialUpdate(StaticPageDTO staticPageDTO) {
        log.debug("Request to partially update StaticPage : {}", staticPageDTO);

        return staticPageRepository
            .findById(staticPageDTO.getId())
            .map(existingStaticPage -> {
                staticPageMapper.partialUpdate(existingStaticPage, staticPageDTO);
                return existingStaticPage;
            })
            .map(staticPageRepository::save)
            .map(staticPageMapper::toDto);
    }

    /**
     * Get all the staticPages.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StaticPageDTO> findAll() {
        log.debug("Request to get all StaticPages");
        return staticPageRepository.findAll().stream().map(staticPageMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one staticPage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StaticPageDTO> findOne(Long id) {
        log.debug("Request to get StaticPage : {}", id);
        return staticPageRepository.findById(id).map(staticPageMapper::toDto);
    }

    /**
     * Delete the staticPage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StaticPage : {}", id);
        staticPageRepository.deleteById(id);
    }
}
