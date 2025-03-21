package at.alpscraft.service.mapper;

import at.alpscraft.domain.StaticPage;
import at.alpscraft.domain.User;
import at.alpscraft.service.dto.StaticPageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StaticPage} and its DTO {@link StaticPageDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface StaticPageMapper extends EntityMapper<StaticPageDTO, StaticPage> {
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorUsername", source = "author.login")
    StaticPageDTO toDto(StaticPage s);

    @Mapping(target = "author", source = "authorId", qualifiedByName = "id")
    StaticPage toEntity(StaticPageDTO staticPageDTO);
}
