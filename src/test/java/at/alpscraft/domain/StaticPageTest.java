package at.alpscraft.domain;

import static at.alpscraft.domain.StaticPageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import at.alpscraft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaticPageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaticPage.class);
        StaticPage staticPage1 = getStaticPageSample1();
        StaticPage staticPage2 = new StaticPage();
        assertThat(staticPage1).isNotEqualTo(staticPage2);

        staticPage2.setId(staticPage1.getId());
        assertThat(staticPage1).isEqualTo(staticPage2);

        staticPage2 = getStaticPageSample2();
        assertThat(staticPage1).isNotEqualTo(staticPage2);
    }
}
