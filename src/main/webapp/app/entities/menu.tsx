import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/post-category">
        <Translate contentKey="global.menu.entities.postCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/blog-post">
        <Translate contentKey="global.menu.entities.blogPost" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/static-page">
        <Translate contentKey="global.menu.entities.staticPage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ui-section">
        <Translate contentKey="global.menu.entities.uiSection" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ui-section-element">
        <Translate contentKey="global.menu.entities.uiSectionElement" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
