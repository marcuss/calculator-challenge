import { IUser } from '@/shared/model/user.model';

import { Operator } from '@/shared/model/enumerations/operator.model';
export interface IRecord {
  id?: string;
  active?: boolean | null;
  operationId?: Operator | null;
  amount?: number;
  userBalance?: number;
  operationRespose?: string;
  date?: Date | null;
  user?: IUser | null;
}

export class Record implements IRecord {
  constructor(
    public id?: string,
    public active?: boolean | null,
    public operationId?: Operator | null,
    public amount?: number,
    public userBalance?: number,
    public operationRespose?: string,
    public date?: Date | null,
    public user?: IUser | null
  ) {
    this.active = this.active ?? false;
  }
}
