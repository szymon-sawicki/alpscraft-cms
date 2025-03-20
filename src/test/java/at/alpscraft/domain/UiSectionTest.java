package at.alpscraft.domain;

import static at.alpscraft.domain.UiSectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import at.alpscraft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UiSectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UiSection.class);
        UiSection uiSection1 = getUiSectionSample1();
        UiSection uiSection2 = new UiSection();
        assertThat(uiSection1).isNotEqualTo(uiSection2);

        uiSection2.setId(uiSection1.getId());
        assertThat(uiSection1).isEqualTo(uiSection2);

        uiSection2 = getUiSectionSample2();
        assertThat(uiSection1).isNotEqualTo(uiSection2);
    }
}
