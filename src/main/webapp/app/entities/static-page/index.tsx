import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StaticPage from './static-page';
import StaticPageDetail from './static-page-detail';
import StaticPageUpdate from './static-page-update';
import StaticPageDeleteDialog from './static-page-delete-dialog';

const StaticPageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StaticPage />} />
    <Route path="new" element={<StaticPageUpdate />} />
    <Route path=":id">
      <Route index element={<StaticPageDetail />} />
      <Route path="edit" element={<StaticPageUpdate />} />
      <Route path="delete" element={<StaticPageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StaticPageRoutes;
