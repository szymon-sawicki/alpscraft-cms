import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, Form, FormGroup, Label } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './static-page.reducer';
import RichTextEditor from 'app/shared/editor/rich-text-editor';
import { processEditorContent } from 'app/shared/util/editor-image-processor';

export const StaticPageUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const staticPageEntity = useAppSelector(state => state.staticPage.entity);
  const loading = useAppSelector(state => state.staticPage.loading);
  const updating = useAppSelector(state => state.staticPage.updating);
  const updateSuccess = useAppSelector(state => state.staticPage.updateSuccess);

  const [formValues, setFormValues] = useState({
    id: '',
    title: '',
    content: '',
    author: '',
  });

  const [contentChanged, setContentChanged] = useState(false);

  const handleClose = () => {
    navigate('/entities/static-page' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (staticPageEntity && !isNew) {
      setFormValues({
        id: staticPageEntity.id?.toString() || '',
        title: staticPageEntity.title || '',
        content: staticPageEntity.content || '',
        author: staticPageEntity.author?.id?.toString() || '',
      });
    }
  }, [staticPageEntity]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const handleInputChange = e => {
    const { name, value } = e.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  const handleContentChange = value => {
    setContentChanged(true);
    setFormValues({
      ...formValues,
      content: value,
    });
    console.log('[StaticPage] Content changed to:', value);
  };

  const handleSubmit = async e => {
    e.preventDefault();
    console.log('[StaticPage] Submitting form with values:', formValues);

    try {
      // Process any base64 images in the content and replace with URLs
      const processedContent = await processEditorContent(formValues.content);

      const entity = {
        id: isNew ? undefined : Number(formValues.id),
        title: formValues.title,
        content: processedContent,
        author: users.find(it => it.id.toString() === formValues.author?.toString()),
      };

      console.log('[StaticPage] Entity to be saved:', entity);

      if (isNew) {
        dispatch(createEntity(entity));
      } else {
        dispatch(updateEntity(entity));
      }
    } catch (error) {
      console.error('Error processing content:', error);
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="alpscraftCmsApp.staticPage.home.createOrEditLabel" data-cy="StaticPageCreateUpdateHeading">
            <Translate contentKey="alpscraftCmsApp.staticPage.home.createOrEditLabel">Create or edit a StaticPage</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <Form onSubmit={handleSubmit}>
              {!isNew ? (
                <FormGroup>
                  <Label for="static-page-id">{translate('global.field.id')}</Label>
                  <ValidatedField name="id" id="static-page-id" type="text" value={formValues.id} readOnly required />
                </FormGroup>
              ) : null}
              <FormGroup>
                <Label for="static-page-title">{translate('alpscraftCmsApp.staticPage.title')}</Label>
                <ValidatedField
                  id="static-page-title"
                  name="title"
                  data-cy="title"
                  type="text"
                  value={formValues.title}
                  onChange={handleInputChange}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Label for="static-page-content">
                  <Translate contentKey="alpscraftCmsApp.staticPage.content">Content</Translate>
                </Label>
                <div>
                  <RichTextEditor
                    value={formValues.content}
                    onChange={handleContentChange}
                    placeholder={translate('alpscraftCmsApp.staticPage.content.placeholder') || 'Write your content here...'}
                  />
                </div>
              </FormGroup>
              <FormGroup>
                <Label for="static-page-author">{translate('alpscraftCmsApp.staticPage.author')}</Label>
                <ValidatedField
                  id="static-page-author"
                  name="author"
                  data-cy="author"
                  type="select"
                  value={formValues.author}
                  onChange={handleInputChange}
                >
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </ValidatedField>
              </FormGroup>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/entities/static-page" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </Form>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StaticPageUpdate;
