import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UiSection from './ui-section';
import UiSectionDetail from './ui-section-detail';
import UiSectionUpdate from './ui-section-update';
import UiSectionDeleteDialog from './ui-section-delete-dialog';

const UiSectionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UiSection />} />
    <Route path="new" element={<UiSectionUpdate />} />
    <Route path=":id">
      <Route index element={<UiSectionDetail />} />
      <Route path="edit" element={<UiSectionUpdate />} />
      <Route path="delete" element={<UiSectionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UiSectionRoutes;
