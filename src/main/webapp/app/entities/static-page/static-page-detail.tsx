import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './static-page.reducer';
import HTMLContentRenderer from 'app/shared/content/html-content-renderer';

export const StaticPageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const staticPageEntity = useAppSelector(state => state.staticPage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="staticPageDetailsHeading">
          <Translate contentKey="alpscraftCmsApp.staticPage.detail.title">StaticPage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{staticPageEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="alpscraftCmsApp.staticPage.title">Title</Translate>
            </span>
          </dt>
          <dd>{staticPageEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="alpscraftCmsApp.staticPage.content">Content</Translate>
            </span>
          </dt>
          <dd>
            <HTMLContentRenderer content={staticPageEntity.content} />
          </dd>
          <dt>
            <Translate contentKey="alpscraftCmsApp.staticPage.author">Author</Translate>
          </dt>
          <dd>{staticPageEntity.author ? staticPageEntity.author.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/static-page" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/static-page/${staticPageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StaticPageDetail;
