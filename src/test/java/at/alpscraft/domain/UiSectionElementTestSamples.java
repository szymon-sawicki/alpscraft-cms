package at.alpscraft.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UiSectionElementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UiSectionElement getUiSectionElementSample1() {
        return new UiSectionElement().id(1L).content("content1");
    }

    public static UiSectionElement getUiSectionElementSample2() {
        return new UiSectionElement().id(2L).content("content2");
    }

    public static UiSectionElement getUiSectionElementRandomSampleGenerator() {
        return new UiSectionElement().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString());
    }
}
