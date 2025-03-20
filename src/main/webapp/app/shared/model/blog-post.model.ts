import { IPostCategory } from 'app/shared/model/post-category.model';
import { IUser } from 'app/shared/model/user.model';

export interface IBlogPost {
  id?: number;
  title?: string;
  content?: string;
  category?: IPostCategory | null;
  author?: IUser | null;
}

export const defaultValue: Readonly<IBlogPost> = {};
