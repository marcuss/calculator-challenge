import { Component, Inject, Vue } from "vue-property-decorator";

import { decimal, required } from "vuelidate/lib/validators";
import dayjs from "dayjs";
import { DATE_TIME_LONG_FORMAT } from "@/shared/date/filters";

import AlertService from "@/shared/alert/alert.service";
import AccountService from "@/account/account.service";
import UserService from "@/entities/user/user.service";

import { IRecord, Record } from "@/shared/model/record.model";
import RecordService from "./record.service";
import { Operator } from "@/shared/model/enumerations/operator.model";

const validations: any = {
  record: {
    userLogin: {
    },
    active: {
    },
    operation: {
      required,
    },
    user: {
      required,
    },
    amount: {
      required,
      decimal,
    },
    userBalance: {
    },
    operationResponse: {
    },
    date: {
    },
  },
};

@Component({
  validations,
})
export default class RecordUpdate extends Vue {
  @Inject('recordService') private recordService: () => RecordService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('userService') private userService: () => UserService;
  @Inject('accountService') private accountService: () => AccountService;

  public record: IRecord = new Record();
  private hasAnyAuthorityValues = {};

  public users: Array<any> = [];
  public operatorValues: string[] = Object.keys(Operator);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.recordId) {
        vm.retrieveRecord(to.params.recordId);
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
    if (this.record.id) {
      this.recordService()
        .update(this.record)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('calculatorApp.record.updated', { param: param.id });
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
      this.recordService()
        .create(this.record)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('calculatorApp.record.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.record[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.record[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.record[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.record[field] = null;
    }
  }

  public retrieveRecord(recordId): void {
    this.recordService()
      .find(recordId)
      .then(res => {
        res.date = new Date(res.date);
        this.record = res;
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

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
    return this.hasAnyAuthorityValues[authorities] ?? false;
  }

  public shouldDisableSaveAdminButton(formIsInvalid: boolean): boolean {
      if (formIsInvalid === false) {
        return false;
      } else {
        return (this.record.operation != 'SQROOT' && this.record.operation != 'RANDOM_STRING') || this.record.user == undefined;
      }
  }

  public shouldDisableSaveRegularButton(validation: any) {
    if  ((this.record.operation == 'SQROOT' || this.record.operation == 'RANDOM_STRING') && this.record.operation != undefined){
      return false;
    }
    else {
      return validation.record.amount.$invalid;
    }
  }
}
