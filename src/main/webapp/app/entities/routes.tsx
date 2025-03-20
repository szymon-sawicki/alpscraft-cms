import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PostCategory from './post-category';
import BlogPost from './blog-post';
import StaticPage from './static-page';
import UiSection from './ui-section';
import UiSectionElement from './ui-section-element';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="post-category/*" element={<PostCategory />} />
        <Route path="blog-post/*" element={<BlogPost />} />
        <Route path="static-page/*" element={<StaticPage />} />
        <Route path="ui-section/*" element={<UiSection />} />
        <Route path="ui-section-element/*" element={<UiSectionElement />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
