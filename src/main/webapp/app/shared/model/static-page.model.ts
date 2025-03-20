import { IUser } from 'app/shared/model/user.model';

export interface IStaticPage {
  id?: number;
  title?: string;
  content?: string;
  author?: IUser | null;
}

export const defaultValue: Readonly<IStaticPage> = {};
