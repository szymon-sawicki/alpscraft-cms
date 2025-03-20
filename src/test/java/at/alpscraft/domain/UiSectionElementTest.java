package at.alpscraft.domain;

import static at.alpscraft.domain.UiSectionElementTestSamples.*;
import static at.alpscraft.domain.UiSectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import at.alpscraft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UiSectionElementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UiSectionElement.class);
        UiSectionElement uiSectionElement1 = getUiSectionElementSample1();
        UiSectionElement uiSectionElement2 = new UiSectionElement();
        assertThat(uiSectionElement1).isNotEqualTo(uiSectionElement2);

        uiSectionElement2.setId(uiSectionElement1.getId());
        assertThat(uiSectionElement1).isEqualTo(uiSectionElement2);

        uiSectionElement2 = getUiSectionElementSample2();
        assertThat(uiSectionElement1).isNotEqualTo(uiSectionElement2);
    }

    @Test
    void uiSectionTest() {
        UiSectionElement uiSectionElement = getUiSectionElementRandomSampleGenerator();
        UiSection uiSectionBack = getUiSectionRandomSampleGenerator();

        uiSectionElement.setUiSection(uiSectionBack);
        assertThat(uiSectionElement.getUiSection()).isEqualTo(uiSectionBack);

        uiSectionElement.uiSection(null);
        assertThat(uiSectionElement.getUiSection()).isNull();
    }
}
