package at.alpscraft.service.mapper;

import at.alpscraft.domain.BlogPost;
import at.alpscraft.domain.PostCategory;
import at.alpscraft.domain.User;
import at.alpscraft.service.dto.BlogPostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BlogPost} and its DTO {@link BlogPostDTO}.
 */
@Mapper(componentModel = "spring", uses = { PostCategoryMapper.class, UserMapper.class })
public interface BlogPostMapper extends EntityMapper<BlogPostDTO, BlogPost> {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorUsername", source = "author.login")
    BlogPostDTO toDto(BlogPost s);

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "id")
    @Mapping(target = "author", source = "authorId", qualifiedByName = "id")
    BlogPost toEntity(BlogPostDTO blogPostDTO);
}
