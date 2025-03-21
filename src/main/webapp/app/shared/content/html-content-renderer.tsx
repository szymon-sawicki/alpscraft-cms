import React from 'react';
import parse from 'html-react-parser';

interface HTMLContentRendererProps {
  content: string;
  className?: string;
}

/**
 * Component that safely renders HTML content
 */
export const HTMLContentRenderer = ({ content, className }: HTMLContentRendererProps) => {
  if (!content) {
    return null;
  }

  return <div className={`html-content ${className || ''}`}>{parse(content)}</div>;
};

export default HTMLContentRenderer;
