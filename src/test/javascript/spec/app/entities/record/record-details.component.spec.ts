/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RecordDetailComponent from '@/entities/record/record-details.vue';
import RecordClass from '@/entities/record/record-details.component';
import RecordService from '@/entities/record/record.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Record Management Detail Component', () => {
    let wrapper: Wrapper<RecordClass>;
    let comp: RecordClass;
    let recordServiceStub: SinonStubbedInstance<RecordService>;

    beforeEach(() => {
      recordServiceStub = sinon.createStubInstance<RecordService>(RecordService);

      wrapper = shallowMount<RecordClass>(RecordDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { recordService: () => recordServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRecord = { id: 'ABC' };
        recordServiceStub.find.resolves(foundRecord);

        // WHEN
        comp.retrieveRecord('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.record).toBe(foundRecord);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRecord = { id: 'ABC' };
        recordServiceStub.find.resolves(foundRecord);

        // WHEN
        comp.beforeRouteEnter({ params: { recordId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.record).toBe(foundRecord);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
