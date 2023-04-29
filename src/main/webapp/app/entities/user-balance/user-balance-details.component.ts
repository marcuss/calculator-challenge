import { Component, Vue, Inject } from 'vue-property-decorator';

import { IUserBalance } from '@/shared/model/user-balance.model';
import UserBalanceService from './user-balance.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class UserBalanceDetails extends Vue {
  @Inject('userBalanceService') private userBalanceService: () => UserBalanceService;
  @Inject('alertService') private alertService: () => AlertService;

  public userBalance: IUserBalance = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.userBalanceId) {
        vm.retrieveUserBalance(to.params.userBalanceId);
      }
    });
  }

  public retrieveUserBalance(userBalanceId) {
    this.userBalanceService()
      .find(userBalanceId)
      .then(res => {
        this.userBalance = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
