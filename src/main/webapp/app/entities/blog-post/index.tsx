import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BlogPost from './blog-post';
import BlogPostDetail from './blog-post-detail';
import BlogPostUpdate from './blog-post-update';
import BlogPostDeleteDialog from './blog-post-delete-dialog';

const BlogPostRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BlogPost />} />
    <Route path="new" element={<BlogPostUpdate />} />
    <Route path=":id">
      <Route index element={<BlogPostDetail />} />
      <Route path="edit" element={<BlogPostUpdate />} />
      <Route path="delete" element={<BlogPostDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BlogPostRoutes;
