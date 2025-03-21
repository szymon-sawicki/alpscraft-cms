import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Card, CardHeader, CardBody } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './blog-post.reducer';
import HTMLContentRenderer from 'app/shared/content/html-content-renderer';

export const BlogPostPublic = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    if (id) {
      dispatch(getEntity(id));
    }
  }, []);

  const blogPostEntity = useAppSelector(state => state.blogPost.entity);

  return (
    <Row className="justify-content-center">
      <Col md="10">
        <Card className="mt-4 mb-4">
          <CardHeader>
            <h1 className="mb-0">{blogPostEntity.title}</h1>
            {blogPostEntity.category && (
              <div className="text-muted mt-2">
                <small>
                  <Translate contentKey="alpscraftCmsApp.blogPost.category">Category</Translate>: {blogPostEntity.category.name}
                </small>
              </div>
            )}
            {blogPostEntity.author && (
              <div className="text-muted">
                <small>
                  <Translate contentKey="alpscraftCmsApp.blogPost.author">Author</Translate>: {blogPostEntity.author.login}
                </small>
              </div>
            )}
          </CardHeader>
          <CardBody>
            {blogPostEntity.content && <HTMLContentRenderer content={blogPostEntity.content} className="blog-content" />}

            <div className="mt-4">
              <Button tag={Link} to="/" color="primary">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
            </div>
          </CardBody>
        </Card>
      </Col>
    </Row>
  );
};

export default BlogPostPublic;
