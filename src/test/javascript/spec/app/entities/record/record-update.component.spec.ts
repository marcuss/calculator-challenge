/* tslint:disable max-line-length */
import { createLocalVue, shallowMount, Wrapper } from "@vue/test-utils";
import sinon, { SinonStubbedInstance } from "sinon";
import Router from "vue-router";
import { ToastPlugin } from "bootstrap-vue";

import dayjs from "dayjs";
import { DATE_TIME_LONG_FORMAT } from "@/shared/date/filters";

import * as config from "@/shared/config/config";
import RecordUpdateComponent from "@/entities/record/record-update.vue";
import RecordClass from "@/entities/record/record-update.component";
import RecordService from "@/entities/record/record.service";

import UserService from "@/entities/user/user.service";
import AlertService from "@/shared/alert/alert.service";
import AccountService from "@/account/account.service";
import TranslationService from "@/locale/translation.service";

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
const translationService = new TranslationService(store, i18n);

localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Record Management Update Component', () => {
    let wrapper: Wrapper<RecordClass>;
    let comp: RecordClass;
    let recordServiceStub: SinonStubbedInstance<RecordService>;

    beforeEach(() => {
      recordServiceStub = sinon.createStubInstance<RecordService>(RecordService);

      wrapper = shallowMount<RecordClass>(RecordUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          recordService: () => recordServiceStub,
          alertService: () => new AlertService(),
          accountService: () => new AccountService(store, translationService, router),
          userService: () => new UserService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 'ABC' };
        comp.record = entity;
        recordServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recordServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.record = entity;
        recordServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(recordServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRecord = { id: 'ABC' };
        recordServiceStub.find.resolves(foundRecord);
        recordServiceStub.retrieve.resolves([foundRecord]);

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
