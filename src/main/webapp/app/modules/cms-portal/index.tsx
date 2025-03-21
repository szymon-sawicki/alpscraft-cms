import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import CmsPortal from './cms-portal';

const CmsPortalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CmsPortal />} />
  </ErrorBoundaryRoutes>
);

export default CmsPortalRoutes;
