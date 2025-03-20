package at.alpscraft.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PostCategory getPostCategorySample1() {
        return new PostCategory().id(1L).name("name1").description("description1");
    }

    public static PostCategory getPostCategorySample2() {
        return new PostCategory().id(2L).name("name2").description("description2");
    }

    public static PostCategory getPostCategoryRandomSampleGenerator() {
        return new PostCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
