import { Component, Vue, Inject } from 'vue-property-decorator';

import { decimal, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import { IUserBalance, UserBalance } from '@/shared/model/user-balance.model';
import UserBalanceService from './user-balance.service';

const validations: any = {
  userBalance: {
    balance: {
      required,
      decimal,
    },
    userLogin: {
    },
  },
};

@Component({
  validations,
})
export default class UserBalanceUpdate extends Vue {
  @Inject('userBalanceService') private userBalanceService: () => UserBalanceService;
  @Inject('alertService') private alertService: () => AlertService;

  public userBalance: IUserBalance = new UserBalance();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.userBalanceId) {
        vm.retrieveUserBalance(to.params.userBalanceId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.userBalance.id) {
      this.userBalanceService()
        .update(this.userBalance)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('calculatorApp.userBalance.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.userBalanceService()
        .create(this.userBalance)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('calculatorApp.userBalance.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveUserBalance(userBalanceId): void {
    this.userBalanceService()
      .find(userBalanceId)
      .then(res => {
        this.userBalance = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
  }
}
