import { IUiSection } from 'app/shared/model/ui-section.model';
import { SectionType } from 'app/shared/model/enumerations/section-type.model';

export interface IUiSectionElement {
  id?: number;
  title?: keyof typeof SectionType;
  content?: string;
  elementOrder?: number;
  uiSection?: IUiSection | null;
}

export const defaultValue: Readonly<IUiSectionElement> = {};
