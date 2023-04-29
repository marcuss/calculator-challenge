import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, decimal, minValue } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IOperation, Operation } from '@/shared/model/operation.model';
import OperationService from './operation.service';
import { Operator } from '@/shared/model/enumerations/operator.model';

const validations: any = {
  operation: {
    operator: {
      required,
    },
    cost: {
      required,
      decimal,
      min: minValue(0),
    },
  },
};

@Component({
  validations,
})
export default class OperationUpdate extends Vue {
  @Inject('operationService') private operationService: () => OperationService;
  @Inject('alertService') private alertService: () => AlertService;

  public operation: IOperation = new Operation();
  public operatorValues: string[] = Object.keys(Operator);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.operationId) {
        vm.retrieveOperation(to.params.operationId);
      }
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
    if (this.operation.id) {
      this.operationService()
        .update(this.operation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('calculatorApp.operation.updated', { param: param.id });
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
      this.operationService()
        .create(this.operation)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('calculatorApp.operation.created', { param: param.id });
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

  public retrieveOperation(operationId): void {
    this.operationService()
      .find(operationId)
      .then(res => {
        this.operation = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
