/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import UserBalanceComponent from '@/entities/user-balance/user-balance.vue';
import UserBalanceClass from '@/entities/user-balance/user-balance.component';
import UserBalanceService from '@/entities/user-balance/user-balance.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('UserBalance Management Component', () => {
    let wrapper: Wrapper<UserBalanceClass>;
    let comp: UserBalanceClass;
    let userBalanceServiceStub: SinonStubbedInstance<UserBalanceService>;

    beforeEach(() => {
      userBalanceServiceStub = sinon.createStubInstance<UserBalanceService>(UserBalanceService);
      userBalanceServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<UserBalanceClass>(UserBalanceComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          userBalanceService: () => userBalanceServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      userBalanceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllUserBalances();
      await comp.$nextTick();

      // THEN
      expect(userBalanceServiceStub.retrieve.called).toBeTruthy();
      expect(comp.userBalances[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      userBalanceServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      expect(userBalanceServiceStub.retrieve.callCount).toEqual(1);

      comp.removeUserBalance();
      await comp.$nextTick();

      // THEN
      expect(userBalanceServiceStub.delete.called).toBeTruthy();
      expect(userBalanceServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
