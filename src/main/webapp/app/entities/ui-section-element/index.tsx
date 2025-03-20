import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UiSectionElement from './ui-section-element';
import UiSectionElementDetail from './ui-section-element-detail';
import UiSectionElementUpdate from './ui-section-element-update';
import UiSectionElementDeleteDialog from './ui-section-element-delete-dialog';

const UiSectionElementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UiSectionElement />} />
    <Route path="new" element={<UiSectionElementUpdate />} />
    <Route path=":id">
      <Route index element={<UiSectionElementDetail />} />
      <Route path="edit" element={<UiSectionElementUpdate />} />
      <Route path="delete" element={<UiSectionElementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UiSectionElementRoutes;
