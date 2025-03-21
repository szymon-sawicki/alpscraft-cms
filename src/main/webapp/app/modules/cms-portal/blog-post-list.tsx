import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Pagination, PaginationItem, PaginationLink } from 'reactstrap';
import { IBlogPost } from 'app/shared/model/blog-post.model';
import parse from 'html-react-parser';

interface BlogPostListProps {
  blogPosts: IBlogPost[];
  itemsPerPage?: number;
}

export const BlogPostList = (props: BlogPostListProps) => {
  const { blogPosts = [], itemsPerPage = 3 } = props;
  const [activePage, setActivePage] = useState(0);

  const handlePaginationClick = (event, page) => {
    event.preventDefault();
    setActivePage(page);
  };

  // Calculate pagination values
  const totalItems = blogPosts.length;
  const totalPages = Math.ceil(totalItems / itemsPerPage);
  const startIndex = activePage * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const currentItems = blogPosts.slice(startIndex, endIndex);

  return (
    <div>
      <Row>
        {currentItems.map((blogPost, i) => (
          <Col md="12" key={`blog-post-${blogPost.id}`} className="mb-4">
            <div className="card">
              <div className="card-body">
                <h5 className="card-title">
                  <Link to={`/blog/${blogPost.id}`}>{blogPost.title}</Link>
                </h5>
                <h6 className="card-subtitle mb-2 text-muted">
                  {blogPost.category?.name || 'Uncategorized'} | Author: {blogPost.author?.login || 'Unknown'}
                </h6>
                <div className="card-text">{parse(blogPost.content?.substring(0, 200) + '...' || '')}</div>
                <Link to={`/blog/${blogPost.id}`} className="btn btn-primary btn-sm mt-2">
                  Read More
                </Link>
              </div>
            </div>
          </Col>
        ))}
      </Row>

      {totalPages > 1 && (
        <Row className="justify-content-center">
          <Pagination>
            <PaginationItem disabled={activePage === 0}>
              <PaginationLink previous onClick={e => handlePaginationClick(e, activePage - 1)} />
            </PaginationItem>
            {[...Array(totalPages)].map((item, i) => (
              <PaginationItem active={i === activePage} key={`page-${i}`}>
                <PaginationLink onClick={e => handlePaginationClick(e, i)}>{i + 1}</PaginationLink>
              </PaginationItem>
            ))}
            <PaginationItem disabled={activePage === totalPages - 1}>
              <PaginationLink next onClick={e => handlePaginationClick(e, activePage + 1)} />
            </PaginationItem>
          </Pagination>
        </Row>
      )}
    </div>
  );
};

export default BlogPostList;
