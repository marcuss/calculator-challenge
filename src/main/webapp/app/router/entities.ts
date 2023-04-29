import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Operation = () => import('@/entities/operation/operation.vue');
// prettier-ignore
const OperationUpdate = () => import('@/entities/operation/operation-update.vue');
// prettier-ignore
const OperationDetails = () => import('@/entities/operation/operation-details.vue');
// prettier-ignore
const Record = () => import('@/entities/record/record.vue');
// prettier-ignore
const RecordUpdate = () => import('@/entities/record/record-update.vue');
// prettier-ignore
const RecordDetails = () => import('@/entities/record/record-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'operation',
      name: 'Operation',
      component: Operation,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'operation/new',
      name: 'OperationCreate',
      component: OperationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'operation/:operationId/edit',
      name: 'OperationEdit',
      component: OperationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'operation/:operationId/view',
      name: 'OperationView',
      component: OperationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'record',
      name: 'Record',
      component: Record,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'record/new',
      name: 'RecordCreate',
      component: RecordUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'record/:recordId/edit',
      name: 'RecordEdit',
      component: RecordUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'record/:recordId/view',
      name: 'RecordView',
      component: RecordDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
