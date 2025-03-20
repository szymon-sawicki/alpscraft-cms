import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ui-section-element.reducer';

export const UiSectionElementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const uiSectionElementEntity = useAppSelector(state => state.uiSectionElement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uiSectionElementDetailsHeading">
          <Translate contentKey="alpscraftCmsApp.uiSectionElement.detail.title">UiSectionElement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{uiSectionElementEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="alpscraftCmsApp.uiSectionElement.title">Title</Translate>
            </span>
          </dt>
          <dd>{uiSectionElementEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="alpscraftCmsApp.uiSectionElement.content">Content</Translate>
            </span>
          </dt>
          <dd>{uiSectionElementEntity.content}</dd>
          <dt>
            <Translate contentKey="alpscraftCmsApp.uiSectionElement.uiSection">Ui Section</Translate>
          </dt>
          <dd>{uiSectionElementEntity.uiSection ? uiSectionElementEntity.uiSection.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ui-section-element" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ui-section-element/${uiSectionElementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UiSectionElementDetail;
