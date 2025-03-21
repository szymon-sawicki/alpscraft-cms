import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUiSections } from 'app/entities/ui-section/ui-section.reducer';
import { getEntities as getUiSectionElements } from 'app/entities/ui-section-element/ui-section-element.reducer';
import { getEntities as getBlogPosts } from 'app/entities/blog-post/blog-post.reducer';
import { SectionType } from 'app/shared/model/enumerations/section-type.model';
import DynamicSection from './dynamic-section';
import BlogPostList from './blog-post-list';
import { Row, Col } from 'reactstrap';
import './cms-portal.scss';

export const CmsPortal = () => {
  const dispatch = useAppDispatch();

  // Selectors for data from Redux store
  const uiSections = useAppSelector(state => state.uiSection.entities);
  const uiSectionElements = useAppSelector(state => state.uiSectionElement.entities);
  const blogPosts = useAppSelector(state => state.blogPost.entities);

  // Loading states
  const uiSectionsLoading = useAppSelector(state => state.uiSection.loading);
  const uiSectionElementsLoading = useAppSelector(state => state.uiSectionElement.loading);
  const blogPostsLoading = useAppSelector(state => state.blogPost.loading);

  const isLoading = uiSectionsLoading || uiSectionElementsLoading || blogPostsLoading;

  useEffect(() => {
    dispatch(getUiSections({}));
    dispatch(getUiSectionElements({}));
    dispatch(getBlogPosts({}));
  }, []);

  // Find sections by type
  const headerSection = uiSections.find(section => section.title === SectionType.HEADER);
  const mainSection = uiSections.find(section => section.title === SectionType.MAIN);
  const footerSection = uiSections.find(section => section.title === SectionType.FOOTER);

  // Find elements by section
  const headerElements = uiSectionElements.filter(element => element.uiSection?.id === headerSection?.id);

  const footerElements = uiSectionElements.filter(element => element.uiSection?.id === footerSection?.id);

  return (
    <div className="cms-portal">
      {isLoading ? (
        <div className="loading-indicator">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        <>
          {/* Header Section */}
          {headerSection && (
            <header>
              <DynamicSection section={headerSection} elements={headerElements} />
            </header>
          )}

          {/* Main Section */}
          {mainSection && (
            <main>
              <DynamicSection section={mainSection} elements={[]} />
              <div className="container">
                <Row>
                  <Col md="8">
                    <BlogPostList blogPosts={blogPosts} />
                  </Col>
                  <Col md="4">
                    <div className="card">
                      <div className="card-body">
                        <h5 className="card-title">Categories</h5>
                        <ul className="list-unstyled">
                          {/* Group blog posts by category and show counts */}
                          {Object.entries(
                            blogPosts.reduce(
                              (acc, post) => {
                                const category = post.category?.name || 'Uncategorized';
                                acc[category] = (acc[category] || 0) + 1;
                                return acc;
                              },
                              {} as Record<string, number>,
                            ),
                          ).map(([category, count]: [string, number]) => (
                            <li key={category}>
                              <a href={`#category-${category}`}>
                                {category} ({count})
                              </a>
                            </li>
                          ))}
                        </ul>
                      </div>
                    </div>
                  </Col>
                </Row>
              </div>
            </main>
          )}

          {/* Footer Section */}
          {footerSection && (
            <footer>
              <DynamicSection section={footerSection} elements={footerElements} />
            </footer>
          )}
        </>
      )}
    </div>
  );
};

export default CmsPortal;
