/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import UserBalanceDetailComponent from '@/entities/user-balance/user-balance-details.vue';
import UserBalanceClass from '@/entities/user-balance/user-balance-details.component';
import UserBalanceService from '@/entities/user-balance/user-balance.service';
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
  describe('UserBalance Management Detail Component', () => {
    let wrapper: Wrapper<UserBalanceClass>;
    let comp: UserBalanceClass;
    let userBalanceServiceStub: SinonStubbedInstance<UserBalanceService>;

    beforeEach(() => {
      userBalanceServiceStub = sinon.createStubInstance<UserBalanceService>(UserBalanceService);

      wrapper = shallowMount<UserBalanceClass>(UserBalanceDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { userBalanceService: () => userBalanceServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundUserBalance = { id: 'ABC' };
        userBalanceServiceStub.find.resolves(foundUserBalance);

        // WHEN
        comp.retrieveUserBalance('ABC');
        await comp.$nextTick();

        // THEN
        expect(comp.userBalance).toBe(foundUserBalance);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundUserBalance = { id: 'ABC' };
        userBalanceServiceStub.find.resolves(foundUserBalance);

        // WHEN
        comp.beforeRouteEnter({ params: { userBalanceId: 'ABC' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.userBalance).toBe(foundUserBalance);
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
