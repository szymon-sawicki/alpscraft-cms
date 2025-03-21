import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getStaticPages } from 'app/entities/static-page/static-page.reducer';
import { getEntities as getUiSections } from 'app/entities/ui-section/ui-section.reducer';
import {
  getEntities as getUiSectionElements,
  createEntity,
  deleteEntity,
  updateEntity,
} from 'app/entities/ui-section-element/ui-section-element.reducer';
import { SectionType } from 'app/shared/model/enumerations/section-type.model';
import { IUiSectionElement } from 'app/shared/model/ui-section-element.model';

export const TopMenuAdmin = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const staticPageList = useAppSelector(state => state.staticPage.entities);
  const uiSectionList = useAppSelector(state => state.uiSection.entities);
  const uiSectionElementList = useAppSelector(state => state.uiSectionElement.entities);
  const loading = useAppSelector(state => state.uiSectionElement.loading);
  const updateSuccess = useAppSelector(state => state.uiSectionElement.updateSuccess);

  const [newMenuItemName, setNewMenuItemName] = useState('');
  const [newMenuItemUrl, setNewMenuItemUrl] = useState('');
  const [newMenuItemOrder, setNewMenuItemOrder] = useState(0);
  const [editing, setEditing] = useState(null);
  const [editName, setEditName] = useState('');
  const [editUrl, setEditUrl] = useState('');
  const [editOrder, setEditOrder] = useState(0);

  console.log('uiSectionList:', uiSectionList);

  const topMenuSection = uiSectionList.find(section => section.title === SectionType.MENU_TOP);
  console.log('Found topMenuSection:', topMenuSection);

  const getAllEntities = () => {
    dispatch(getUiSectionElements({}));
  };

  const sortEntities = () => {
    getAllEntities();
  };

  useEffect(() => {
    sortEntities();
  }, []);

  useEffect(() => {
    dispatch(getStaticPages({}));
    dispatch(getUiSections({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      getAllEntities();
    }
  }, [updateSuccess]);

  const filteredMenuItems = topMenuSection ? uiSectionElementList.filter(element => element.uiSection?.id === topMenuSection.id) : [];

  console.log('topMenuSection:', topMenuSection);
  console.log('uiSectionElementList:', uiSectionElementList);
  console.log('filteredMenuItems:', filteredMenuItems);

  const handleSyncList = () => {
    sortEntities();
  };

  const addNewMenuItem = () => {
    if (!topMenuSection || !newMenuItemName || !newMenuItemUrl) return;

    // Store the name and url in the content field as JSON
    const menuItemData = {
      name: newMenuItemName,
      url: newMenuItemUrl,
    };

    const entity: IUiSectionElement = {
      title: SectionType.MENU_TOP,
      content: JSON.stringify(menuItemData),
      elementOrder: newMenuItemOrder,
      uiSection: topMenuSection,
    };

    console.log('Creating new menu item:', entity);
    dispatch(createEntity(entity));
    setNewMenuItemName('');
    setNewMenuItemUrl('');
    setNewMenuItemOrder(0);
  };

  const startEditing = item => {
    setEditing(item.id);

    try {
      // Try to parse content as JSON
      const menuItemData = JSON.parse(item.content);
      if (menuItemData.name) {
        setEditName(menuItemData.name);
        setEditUrl(menuItemData.url || '');
      } else {
        setEditName(item.title === SectionType.MENU_TOP ? 'blog' : item.title);
        setEditUrl(item.content);
      }
    } catch (e) {
      // If not JSON, use the old format
      setEditName(item.title === SectionType.MENU_TOP ? 'blog' : item.title);
      setEditUrl(item.content);
    }

    setEditOrder(item.elementOrder);
  };

  const cancelEditing = () => {
    setEditing(null);
  };

  const saveEditedItem = item => {
    // Store the name and url in the content field as JSON
    const menuItemData = {
      name: editName,
      url: editUrl,
    };

    const entity = {
      ...item,
      content: JSON.stringify(menuItemData),
      elementOrder: editOrder,
    };

    dispatch(updateEntity(entity));
    setEditing(null);
  };

  const deleteMenuItem = id => {
    dispatch(deleteEntity(id));
  };

  // Helper function to resolve menu item name
  const resolveMenuItemName = item => {
    try {
      // Try to parse content as JSON
      const menuItemData = JSON.parse(item.content);
      if (menuItemData.name) {
        return menuItemData.name;
      }
    } catch (e) {
      // If not JSON, use the old format
    }

    // Fallback to old format
    if (item.title === 'blog') return 'Blog (Home page)';

    const staticPage = staticPageList.find(page => {
      return page.id.toString() === item.title;
    });

    return staticPage?.title || item.title;
  };

  // Helper function to resolve menu item URL
  const resolveMenuItemUrl = item => {
    try {
      // Try to parse content as JSON
      const menuItemData = JSON.parse(item.content);
      if (menuItemData.url) {
        return menuItemData.url;
      }
    } catch (e) {
      // If not JSON, use the content field directly
    }

    return item.content;
  };

  return (
    <div>
      <h2 id="top-menu-heading" data-cy="TopMenuHeading">
        <Translate contentKey="global.menu.admin.topMenu">Top Menu</Translate>
      </h2>
      <div className="d-flex justify-content-end">
        <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
          <FontAwesomeIcon icon="sync" spin={loading} />{' '}
          <Translate contentKey="alpscraftCmsApp.uiSectionElement.home.refreshListLabel">Refresh List</Translate>
        </Button>
      </div>
      <div className="table-responsive">
        {topMenuSection ? (
          <div>
            <h4>Add new menu item</h4>
            <div className="mb-3 d-flex">
              <div className="me-2">
                <label className="form-label">Name</label>
                <select
                  className="form-control"
                  value={newMenuItemName}
                  onChange={e => {
                    setNewMenuItemName(e.target.value);
                    if (e.target.value === 'blog') {
                      setNewMenuItemUrl('/');
                    } else {
                      const selectedPage = staticPageList.find(page => page.id.toString() === e.target.value);
                      if (selectedPage) {
                        setNewMenuItemUrl(`/page/${selectedPage.slug}`);
                      }
                    }
                  }}
                >
                  <option value="">Select a page</option>
                  <option value="blog">Blog (Home page)</option>
                  {staticPageList.map(page => (
                    <option key={page.id} value={page.id.toString()}>
                      {page.title}
                    </option>
                  ))}
                </select>
              </div>
              <div className="me-2">
                <label className="form-label">URL</label>
                <input
                  type="text"
                  className="form-control"
                  value={newMenuItemUrl}
                  onChange={e => setNewMenuItemUrl(e.target.value)}
                  readOnly={newMenuItemName === 'blog' || staticPageList.some(page => page.id.toString() === newMenuItemName)}
                />
              </div>
              <div className="me-2" style={{ width: '100px' }}>
                <label className="form-label">Order</label>
                <input
                  type="number"
                  className="form-control"
                  value={newMenuItemOrder}
                  onChange={e => setNewMenuItemOrder(parseInt(e.target.value, 10))}
                />
              </div>
              <div style={{ alignSelf: 'flex-end' }}>
                <Button color="primary" onClick={addNewMenuItem}>
                  <FontAwesomeIcon icon="plus" /> <Translate contentKey="entity.action.add">Add</Translate>
                </Button>
              </div>
            </div>

            <h4>Menu Items</h4>
            {filteredMenuItems.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>URL</th>
                    <th>Order</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredMenuItems.map((item, i) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      {editing === item.id ? (
                        <>
                          <td>
                            <select
                              className="form-control"
                              value={editName}
                              onChange={e => {
                                setEditName(e.target.value);
                                if (e.target.value === 'blog') {
                                  setEditUrl('/');
                                } else {
                                  const selectedPage = staticPageList.find(page => page.id.toString() === e.target.value);
                                  if (selectedPage) {
                                    setEditUrl(`/page/${selectedPage.slug}`);
                                  }
                                }
                              }}
                            >
                              <option value="blog">Blog (Home page)</option>
                              {staticPageList.map(page => (
                                <option key={page.id} value={page.id.toString()}>
                                  {page.title}
                                </option>
                              ))}
                            </select>
                          </td>
                          <td>
                            <input
                              type="text"
                              className="form-control"
                              value={editUrl}
                              onChange={e => setEditUrl(e.target.value)}
                              readOnly={editName === 'blog' || staticPageList.some(page => page.id.toString() === editName)}
                            />
                          </td>
                          <td>
                            <input
                              type="number"
                              className="form-control"
                              value={editOrder}
                              onChange={e => setEditOrder(parseInt(e.target.value, 10))}
                            />
                          </td>
                          <td>
                            <Button color="primary" size="sm" onClick={() => saveEditedItem(item)}>
                              <FontAwesomeIcon icon="save" /> <Translate contentKey="entity.action.save">Save</Translate>
                            </Button>{' '}
                            <Button color="secondary" size="sm" onClick={cancelEditing}>
                              <FontAwesomeIcon icon="ban" /> <Translate contentKey="entity.action.cancel">Cancel</Translate>
                            </Button>
                          </td>
                        </>
                      ) : (
                        <>
                          <td>{resolveMenuItemName(item)}</td>
                          <td>{resolveMenuItemUrl(item)}</td>
                          <td>{item.elementOrder}</td>
                          <td className="text-end">
                            <div className="btn-group flex-btn-group-container">
                              <Button onClick={() => startEditing(item)} color="primary" size="sm" data-cy="entityEditButton">
                                <FontAwesomeIcon icon="pencil-alt" />{' '}
                                <span className="d-none d-md-inline">
                                  <Translate contentKey="entity.action.edit">Edit</Translate>
                                </span>
                              </Button>
                              <Button onClick={() => deleteMenuItem(item.id)} color="danger" size="sm" data-cy="entityDeleteButton">
                                <FontAwesomeIcon icon="trash" />{' '}
                                <span className="d-none d-md-inline">
                                  <Translate contentKey="entity.action.delete">Delete</Translate>
                                </span>
                              </Button>
                            </div>
                          </td>
                        </>
                      )}
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">No menu items found</div>
            )}
          </div>
        ) : (
          <div className="alert alert-warning">Top Menu section not found. Please run the database migration first.</div>
        )}
      </div>
    </div>
  );
};

export default TopMenuAdmin;
