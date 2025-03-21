import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './blog-post.reducer';
import HTMLContentRenderer from 'app/shared/content/html-content-renderer';

export const BlogPostDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const blogPostEntity = useAppSelector(state => state.blogPost.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="blogPostDetailsHeading">
          <Translate contentKey="alpscraftCmsApp.blogPost.detail.title">BlogPost</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{blogPostEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="alpscraftCmsApp.blogPost.title">Title</Translate>
            </span>
          </dt>
          <dd>{blogPostEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="alpscraftCmsApp.blogPost.content">Content</Translate>
            </span>
          </dt>
          <dd>
            <HTMLContentRenderer content={blogPostEntity.content} />
          </dd>
          <dt>
            <Translate contentKey="alpscraftCmsApp.blogPost.category">Category</Translate>
          </dt>
          <dd>{blogPostEntity.category ? blogPostEntity.category.name : ''}</dd>
          <dt>
            <Translate contentKey="alpscraftCmsApp.blogPost.author">Author</Translate>
          </dt>
          <dd>{blogPostEntity.author ? blogPostEntity.author.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/blog-post" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/blog-post/${blogPostEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BlogPostDetail;
