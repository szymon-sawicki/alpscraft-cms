package at.alpscraft.domain;

import static at.alpscraft.domain.PostCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import at.alpscraft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostCategory.class);
        PostCategory postCategory1 = getPostCategorySample1();
        PostCategory postCategory2 = new PostCategory();
        assertThat(postCategory1).isNotEqualTo(postCategory2);

        postCategory2.setId(postCategory1.getId());
        assertThat(postCategory1).isEqualTo(postCategory2);

        postCategory2 = getPostCategorySample2();
        assertThat(postCategory1).isNotEqualTo(postCategory2);
    }
}
