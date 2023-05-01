import { IUser } from '@/shared/model/user.model';

export interface IUserBalance {
  id?: string;
  balance?: number;
  userLogin?: string;
  user?: IUser | null;
}

export class UserBalance implements IUserBalance {
  constructor(public id?: string, public balance?: number, public userLogin?: string, public user?: IUser | null) {}
}
