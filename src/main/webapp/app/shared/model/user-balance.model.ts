import { IUser } from '@/shared/model/user.model';

export interface IUserBalance {
  id?: string;
  balance?: number;
  user?: IUser | null;
}

export class UserBalance implements IUserBalance {
  constructor(public id?: string, public balance?: number, public user?: IUser | null) {}
}
