import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { SectionType } from 'app/shared/model/enumerations/section-type.model';
import { createEntity, getEntity, reset, updateEntity } from './ui-section.reducer';

export const UiSectionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const uiSectionEntity = useAppSelector(state => state.uiSection.entity);
  const loading = useAppSelector(state => state.uiSection.loading);
  const updating = useAppSelector(state => state.uiSection.updating);
  const updateSuccess = useAppSelector(state => state.uiSection.updateSuccess);
  const sectionTypeValues = Object.keys(SectionType);

  const handleClose = () => {
    navigate('/ui-section');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...uiSectionEntity,
      ...values,
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
          title: 'HEADER',
          ...uiSectionEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="alpscraftCmsApp.uiSection.home.createOrEditLabel" data-cy="UiSectionCreateUpdateHeading">
            <Translate contentKey="alpscraftCmsApp.uiSection.home.createOrEditLabel">Create or edit a UiSection</Translate>
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
                  id="ui-section-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('alpscraftCmsApp.uiSection.title')}
                id="ui-section-title"
                name="title"
                data-cy="title"
                type="select"
              >
                {sectionTypeValues.map(sectionType => (
                  <option value={sectionType} key={sectionType}>
                    {translate(`alpscraftCmsApp.SectionType.${sectionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('alpscraftCmsApp.uiSection.cssClass')}
                id="ui-section-cssClass"
                name="cssClass"
                data-cy="cssClass"
                type="text"
              />
              <ValidatedField
                label={translate('alpscraftCmsApp.uiSection.content')}
                id="ui-section-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ui-section" replace color="info">
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

export default UiSectionUpdate;
