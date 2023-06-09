import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import OperationService from './operation/operation.service';
import RecordService from './record/record.service';
import UserBalanceService from './user-balance/user-balance.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('operationService') private operationService = () => new OperationService();
  @Provide('recordService') private recordService = () => new RecordService();
  @Provide('userBalanceService') private userBalanceService = () => new UserBalanceService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
