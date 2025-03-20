package at.alpscraft.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BlogPostAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBlogPostAllPropertiesEquals(BlogPost expected, BlogPost actual) {
        assertBlogPostAutoGeneratedPropertiesEquals(expected, actual);
        assertBlogPostAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBlogPostAllUpdatablePropertiesEquals(BlogPost expected, BlogPost actual) {
        assertBlogPostUpdatableFieldsEquals(expected, actual);
        assertBlogPostUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBlogPostAutoGeneratedPropertiesEquals(BlogPost expected, BlogPost actual) {
        assertThat(actual)
            .as("Verify BlogPost auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBlogPostUpdatableFieldsEquals(BlogPost expected, BlogPost actual) {
        assertThat(actual)
            .as("Verify BlogPost relevant properties")
            .satisfies(a -> assertThat(a.getTitle()).as("check title").isEqualTo(expected.getTitle()))
            .satisfies(a -> assertThat(a.getContent()).as("check content").isEqualTo(expected.getContent()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBlogPostUpdatableRelationshipsEquals(BlogPost expected, BlogPost actual) {
        assertThat(actual)
            .as("Verify BlogPost relationships")
            .satisfies(a -> assertThat(a.getCategory()).as("check category").isEqualTo(expected.getCategory()));
    }
}
