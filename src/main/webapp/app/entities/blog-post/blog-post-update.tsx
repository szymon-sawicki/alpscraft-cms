import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, Form, FormGroup, Label, Input } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getPostCategories } from 'app/entities/post-category/post-category.reducer';
import { createEntity, getEntity, reset, updateEntity } from './blog-post.reducer';
import RichTextEditor from 'app/shared/editor/rich-text-editor';
import { processEditorContent } from 'app/shared/util/editor-image-processor';

export const BlogPostUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const postCategories = useAppSelector(state => state.postCategory.entities);
  const blogPostEntity = useAppSelector(state => state.blogPost.entity);
  const loading = useAppSelector(state => state.blogPost.loading);
  const updating = useAppSelector(state => state.blogPost.updating);
  const updateSuccess = useAppSelector(state => state.blogPost.updateSuccess);

  const [formValues, setFormValues] = useState({
    id: '',
    title: '',
    content: '',
    categoryId: '',
    authorId: '',
  });

  const handleClose = () => {
    navigate('/entities/blog-post');
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
    if (blogPostEntity && !isNew) {
      console.log('[BlogPost] Setting form values from entity:', blogPostEntity);
      setFormValues({
        id: blogPostEntity.id?.toString() || '',
        title: blogPostEntity.title || '',
        content: blogPostEntity.content || '',
        categoryId: blogPostEntity.category?.id?.toString() || '',
        authorId: blogPostEntity.author?.id?.toString() || '',
      });
    }
  }, [blogPostEntity]);

  const handleInputChange = e => {
    const { name, value } = e.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
    console.log(`[BlogPost] Field ${name} changed to:`, value);
  };

  const handleContentChange = value => {
    setFormValues({
      ...formValues,
      content: value,
    });
    console.log('[BlogPost] Content changed to:', value);
  };

  const handleSubmit = async e => {
    e.preventDefault();
    console.log('[BlogPost] Submitting form with values:', formValues);

    try {
      // Process any base64 images in the content and replace with URLs
      const processedContent = await processEditorContent(formValues.content);

      const entity = {
        id: isNew ? undefined : Number(formValues.id),
        title: formValues.title,
        content: processedContent,
        category: formValues.categoryId ? { id: Number(formValues.categoryId) } : null,
        author: formValues.authorId ? { id: Number(formValues.authorId) } : null,
      };

      console.log('[BlogPost] Entity to be saved:', entity);

      if (isNew) {
        dispatch(createEntity(entity))
          .unwrap()
          .then(() => {
            console.log('[BlogPost] Navigation after successful save to:', '/entities/blog-post');
            navigate('/entities/blog-post');
          })
          .catch(err => {
            console.error('[BlogPost] Error creating entity:', err);
          });
      } else {
        dispatch(updateEntity(entity))
          .unwrap()
          .then(() => {
            console.log('[BlogPost] Navigation after successful save to:', '/entities/blog-post');
            navigate('/entities/blog-post');
          })
          .catch(err => {
            console.error('[BlogPost] Error updating entity:', err);
          });
      }
    } catch (error) {
      console.error('Error processing content:', error);
    }
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
            <Form onSubmit={handleSubmit}>
              {!isNew ? (
                <FormGroup>
                  <Label for="blog-post-id">{translate('global.field.id')}</Label>
                  <Input id="blog-post-id" name="id" type="text" value={formValues.id} readOnly required />
                </FormGroup>
              ) : null}
              <FormGroup>
                <Label for="blog-post-title">{translate('alpscraftCmsApp.blogPost.title')}</Label>
                <Input
                  id="blog-post-title"
                  name="title"
                  data-cy="title"
                  type="text"
                  value={formValues.title}
                  onChange={handleInputChange}
                  required
                />
              </FormGroup>
              <FormGroup>
                <Label for="blog-post-content">
                  <Translate contentKey="alpscraftCmsApp.blogPost.content.label">Content</Translate>
                </Label>
                <div>
                  <RichTextEditor
                    value={formValues.content}
                    onChange={handleContentChange}
                    placeholder={translate('alpscraftCmsApp.blogPost.content.placeholder') || 'Write your content here...'}
                  />
                </div>
              </FormGroup>
              <FormGroup>
                <Label for="blog-post-category">{translate('alpscraftCmsApp.blogPost.category')}</Label>
                <Input
                  id="blog-post-category"
                  name="categoryId"
                  data-cy="category"
                  type="select"
                  value={formValues.categoryId}
                  onChange={handleInputChange}
                >
                  <option value="" key="0" />
                  {postCategories
                    ? postCategories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </Input>
              </FormGroup>
              <FormGroup>
                <Label for="blog-post-author">{translate('alpscraftCmsApp.blogPost.author')}</Label>
                <Input
                  id="blog-post-author"
                  name="authorId"
                  data-cy="author"
                  type="select"
                  value={formValues.authorId}
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
                </Input>
              </FormGroup>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/entities/blog-post" replace color="info">
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

export default BlogPostUpdate;
