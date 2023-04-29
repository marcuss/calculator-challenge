import { Operator } from '@/shared/model/enumerations/operator.model';
export interface IOperation {
  id?: string;
  operator?: Operator;
  cost?: number;
}

export class Operation implements IOperation {
  constructor(public id?: string, public operator?: Operator, public cost?: number) {}
}
