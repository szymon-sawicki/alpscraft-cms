import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPostCategories } from 'app/entities/post-category/post-category.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './blog-post.reducer';
import RichTextEditor from 'app/shared/editor/rich-text-editor';

export const BlogPostUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postCategories = useAppSelector(state => state.postCategory.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const blogPostEntity = useAppSelector(state => state.blogPost.entity);
  const loading = useAppSelector(state => state.blogPost.loading);
  const updating = useAppSelector(state => state.blogPost.updating);
  const updateSuccess = useAppSelector(state => state.blogPost.updateSuccess);

  const handleClose = () => {
    navigate('/blog-post');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostCategories({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...blogPostEntity,
      ...values,
      category: postCategories.find(it => it.id.toString() === values.category?.toString()),
      author: users.find(it => it.id.toString() === values.author?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...blogPostEntity,
          category: blogPostEntity?.category?.id,
          author: blogPostEntity?.author?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="alpscraftCmsApp.blogPost.home.createOrEditLabel" data-cy="BlogPostCreateUpdateHeading">
            <Translate contentKey="alpscraftCmsApp.blogPost.home.createOrEditLabel">Create or edit a BlogPost</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="blog-post-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('alpscraftCmsApp.blogPost.title')}
                id="blog-post-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Row className="mb-3">
                <Col md="3">
                  <label htmlFor="blog-post-content">
                    <Translate contentKey="alpscraftCmsApp.blogPost.content.label">Content</Translate>
                  </label>
                </Col>
                <Col md="9">
                  <ValidatedField
                    id="blog-post-content"
                    name="content"
                    data-cy="content"
                    type="hidden"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <RichTextEditor
                    value={blogPostEntity.content || ''}
                    onChange={value => {
                      // This is a workaround since we can't directly use CustomInput
                      const event = new Event('change', { bubbles: true });
                      const element = document.getElementById('blog-post-content');
                      if (element) {
                        const input = element as HTMLInputElement;
                        input.value = value;
                        input.dispatchEvent(event);
                      }
                    }}
                    placeholder={translate('alpscraftCmsApp.blogPost.content.placeholder')}
                  />
                </Col>
              </Row>
              <ValidatedField
                id="blog-post-category"
                name="category"
                data-cy="category"
                label={translate('alpscraftCmsApp.blogPost.category')}
                type="select"
              >
                <option value="" key="0" />
                {postCategories
                  ? postCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="blog-post-author"
                name="author"
                data-cy="author"
                label={translate('alpscraftCmsApp.blogPost.author')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/blog-post" replace color="info">
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
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BlogPostUpdate;
