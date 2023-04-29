import { Component, Vue, Inject } from 'vue-property-decorator';

import { IRecord } from '@/shared/model/record.model';
import RecordService from './record.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class RecordDetails extends Vue {
  @Inject('recordService') private recordService: () => RecordService;
  @Inject('alertService') private alertService: () => AlertService;

  public record: IRecord = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.recordId) {
        vm.retrieveRecord(to.params.recordId);
      }
    });
  }

  public retrieveRecord(recordId) {
    this.recordService()
      .find(recordId)
      .then(res => {
        this.record = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
