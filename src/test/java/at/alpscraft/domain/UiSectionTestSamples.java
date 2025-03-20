package at.alpscraft.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UiSectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UiSection getUiSectionSample1() {
        return new UiSection().id(1L).cssClass("cssClass1").content("content1");
    }

    public static UiSection getUiSectionSample2() {
        return new UiSection().id(2L).cssClass("cssClass2").content("content2");
    }

    public static UiSection getUiSectionRandomSampleGenerator() {
        return new UiSection().id(longCount.incrementAndGet()).cssClass(UUID.randomUUID().toString()).content(UUID.randomUUID().toString());
    }
}
