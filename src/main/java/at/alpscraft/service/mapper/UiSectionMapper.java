package at.alpscraft.service.mapper;

import at.alpscraft.domain.UiSection;
import at.alpscraft.service.dto.UiSectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UiSection} and its DTO {@link UiSectionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UiSectionMapper extends EntityMapper<UiSectionDTO, UiSection> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UiSection toEntity(Long id);
}
