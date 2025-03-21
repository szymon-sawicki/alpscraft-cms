import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './blog-post.reducer';
import parse from 'html-react-parser';

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
    <div className="container py-5">
      <Row>
        <Col md={8} className="mx-auto">
          {blogPostEntity?.id ? (
            <div className="blog-post">
              <h1 className="display-4 mb-3">{blogPostEntity.title}</h1>

              <div className="text-muted mb-4">
                <span className="badge bg-primary me-2">{blogPostEntity.category?.name}</span>
                <span>by {blogPostEntity.author?.login || 'Unknown'}</span>
              </div>

              <div className="blog-content">{parse(blogPostEntity.content || '')}</div>

              <div className="mt-5">
                <Link to="/" className="btn btn-info">
                  <FontAwesomeIcon icon="arrow-left" />
                  <span className="d-none d-md-inline ms-1">Back to Portal</span>
                </Link>
              </div>
            </div>
          ) : (
            <div className="alert alert-warning">Blog post not found</div>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BlogPostPublic;
