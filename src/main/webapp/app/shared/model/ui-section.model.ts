import { SectionType } from 'app/shared/model/enumerations/section-type.model';

export interface IUiSection {
  id?: number;
  title?: keyof typeof SectionType;
  cssClass?: string | null;
  content?: string;
}

export const defaultValue: Readonly<IUiSection> = {};
