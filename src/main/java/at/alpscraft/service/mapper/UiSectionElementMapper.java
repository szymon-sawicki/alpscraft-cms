package at.alpscraft.service.mapper;

import at.alpscraft.domain.UiSection;
import at.alpscraft.domain.UiSectionElement;
import at.alpscraft.service.dto.UiSectionElementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UiSectionElement} and its DTO {@link UiSectionElementDTO}.
 */
@Mapper(componentModel = "spring", uses = { UiSectionMapper.class })
public interface UiSectionElementMapper extends EntityMapper<UiSectionElementDTO, UiSectionElement> {
    @Mapping(target = "uiSectionId", source = "uiSection.id")
    @Mapping(target = "uiSectionTitle", source = "uiSection.title")
    UiSectionElementDTO toDto(UiSectionElement s);

    @Mapping(target = "uiSection", source = "uiSectionId", qualifiedByName = "id")
    UiSectionElement toEntity(UiSectionElementDTO uiSectionElementDTO);
}
