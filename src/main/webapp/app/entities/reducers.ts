import postCategory from 'app/entities/post-category/post-category.reducer';
import blogPost from 'app/entities/blog-post/blog-post.reducer';
import staticPage from 'app/entities/static-page/static-page.reducer';
import uiSection from 'app/entities/ui-section/ui-section.reducer';
import uiSectionElement from 'app/entities/ui-section-element/ui-section-element.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  postCategory,
  blogPost,
  staticPage,
  uiSection,
  uiSectionElement,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
