import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ui-section.reducer';

export const UiSectionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const uiSectionEntity = useAppSelector(state => state.uiSection.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uiSectionDetailsHeading">
          <Translate contentKey="alpscraftCmsApp.uiSection.detail.title">UiSection</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{uiSectionEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="alpscraftCmsApp.uiSection.title">Title</Translate>
            </span>
          </dt>
          <dd>{uiSectionEntity.title}</dd>
          <dt>
            <span id="cssClass">
              <Translate contentKey="alpscraftCmsApp.uiSection.cssClass">Css Class</Translate>
            </span>
          </dt>
          <dd>{uiSectionEntity.cssClass}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="alpscraftCmsApp.uiSection.content">Content</Translate>
            </span>
          </dt>
          <dd>{uiSectionEntity.content}</dd>
        </dl>
        <Button tag={Link} to="/ui-section" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ui-section/${uiSectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UiSectionDetail;
