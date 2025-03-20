import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUiSections } from 'app/entities/ui-section/ui-section.reducer';
import { SectionType } from 'app/shared/model/enumerations/section-type.model';
import { createEntity, getEntity, reset, updateEntity } from './ui-section-element.reducer';

export const UiSectionElementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const uiSections = useAppSelector(state => state.uiSection.entities);
  const uiSectionElementEntity = useAppSelector(state => state.uiSectionElement.entity);
  const loading = useAppSelector(state => state.uiSectionElement.loading);
  const updating = useAppSelector(state => state.uiSectionElement.updating);
  const updateSuccess = useAppSelector(state => state.uiSectionElement.updateSuccess);
  const sectionTypeValues = Object.keys(SectionType);

  const handleClose = () => {
    navigate('/ui-section-element');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUiSections({}));
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
      ...uiSectionElementEntity,
      ...values,
      uiSection: uiSections.find(it => it.id.toString() === values.uiSection?.toString()),
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
          ...uiSectionElementEntity,
          uiSection: uiSectionElementEntity?.uiSection?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="alpscraftCmsApp.uiSectionElement.home.createOrEditLabel" data-cy="UiSectionElementCreateUpdateHeading">
            <Translate contentKey="alpscraftCmsApp.uiSectionElement.home.createOrEditLabel">Create or edit a UiSectionElement</Translate>
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
                  id="ui-section-element-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('alpscraftCmsApp.uiSectionElement.title')}
                id="ui-section-element-title"
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
                label={translate('alpscraftCmsApp.uiSectionElement.content')}
                id="ui-section-element-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="ui-section-element-uiSection"
                name="uiSection"
                data-cy="uiSection"
                label={translate('alpscraftCmsApp.uiSectionElement.uiSection')}
                type="select"
              >
                <option value="" key="0" />
                {uiSections
                  ? uiSections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ui-section-element" replace color="info">
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

export default UiSectionElementUpdate;
