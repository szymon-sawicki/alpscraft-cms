import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-category.reducer';

export const PostCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postCategoryEntity = useAppSelector(state => state.postCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postCategoryDetailsHeading">
          <Translate contentKey="alpscraftCmsApp.postCategory.detail.title">PostCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="alpscraftCmsApp.postCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{postCategoryEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="alpscraftCmsApp.postCategory.description">Description</Translate>
            </span>
          </dt>
          <dd>{postCategoryEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/post-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-category/${postCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostCategoryDetail;
