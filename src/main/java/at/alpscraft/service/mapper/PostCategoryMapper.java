package at.alpscraft.service.mapper;

import at.alpscraft.domain.PostCategory;
import at.alpscraft.service.dto.PostCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostCategory} and its DTO {@link PostCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostCategoryMapper extends EntityMapper<PostCategoryDTO, PostCategory> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostCategory toEntity(Long id);
}
