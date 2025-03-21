import React from 'react';
import { IUiSection } from 'app/shared/model/ui-section.model';
import { IUiSectionElement } from 'app/shared/model/ui-section-element.model';
import { Link } from 'react-router-dom';
import parse from 'html-react-parser';

interface DynamicSectionProps {
  section: IUiSection;
  elements: IUiSectionElement[];
}

export const DynamicSection = (props: DynamicSectionProps) => {
  const { section, elements } = props;

  const renderLinks = () => {
    return elements.map(element => {
      // Parse content format: "Text|URL"
      const [text, url] = element.content?.split('|') || ['', ''];

      // If URL starts with http or https, create an external link
      if (url.startsWith('http')) {
        return (
          <a key={element.id} href={url} className="me-3" target="_blank" rel="noopener noreferrer">
            {text}
          </a>
        );
      }

      // Otherwise, create an internal React Router Link
      return (
        <Link key={element.id} to={url} className="me-3">
          {text}
        </Link>
      );
    });
  };

  return (
    <div className={section.cssClass || ''}>
      {parse(section.content || '')}
      {elements.length > 0 && <div className="d-flex flex-wrap">{renderLinks()}</div>}
    </div>
  );
};

export default DynamicSection;
