package at.alpscraft.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StaticPageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StaticPage getStaticPageSample1() {
        return new StaticPage().id(1L).title("title1").content("content1");
    }

    public static StaticPage getStaticPageSample2() {
        return new StaticPage().id(2L).title("title2").content("content2");
    }

    public static StaticPage getStaticPageRandomSampleGenerator() {
        return new StaticPage().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).content(UUID.randomUUID().toString());
    }
}
