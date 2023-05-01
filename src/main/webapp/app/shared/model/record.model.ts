import { IUser } from "@/shared/model/user.model";

import { Operator } from "@/shared/model/enumerations/operator.model";

export interface IRecord {
  id?: string;
  userLogin?: string | null;
  active?: boolean | null;
  operation?: Operator;
  amount?: number;
  userBalance?: number;
  operationResponse?: string | null;
  date?: Date | null;
  user?: IUser | null;
}

export class Record implements IRecord {
  constructor(
    public id?: string,
    public userLogin?: string | null,
    public active?: boolean,
    public operation?: Operator,
    public amount?: number,
    public userBalance?: number,
    public operationResponse?: string,
    public date?: Date | null,
    public user?: IUser | null
  ) {
    this.active = this.active ?? false;
  }
}
