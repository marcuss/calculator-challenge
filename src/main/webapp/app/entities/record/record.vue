<template>
  <div>
    <h2 id="page-heading" data-cy="RecordHeading">
      <span v-text="$t('calculatorApp.record.home.title')" id="record-heading">Records</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('calculatorApp.record.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'RecordCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-record"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('calculatorApp.record.home.createLabel')"> Create a new Record </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && records && records.length === 0">
      <span v-text="$t('calculatorApp.record.home.notFound')">No records found</span>
    </div>
    <div class="table-responsive" v-if="records && records.length > 0">
      <table class="table table-striped" aria-describedby="records">
        <thead>
          <tr>
            <th scope="row" v-if="hasAnyAuthority('ROLE_ADMIN')" v-on:click="changeOrder('active')">
              <span v-text="$t('calculatorApp.record.active')">Active</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-if="hasAnyAuthority('ROLE_ADMIN')" v-on:click="changeOrder('user.id')">
              <span v-text="$t('calculatorApp.record.user')">User</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('operation')">
              <span v-text="$t('calculatorApp.record.operation')">Operation</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'operation'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('amount')">
              <span v-text="$t('calculatorApp.record.amount')">Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'amount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('operationResponse')">
              <span v-text="$t('calculatorApp.record.operationResponse')">Operation Response</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'operationResponse'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('userBalance')">
              <span v-text="$t('calculatorApp.record.userBalance')">User Balance</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'userBalance'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('date')">
              <span v-text="$t('calculatorApp.record.date')">Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'date'"></jhi-sort-indicator>
            </th>

            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in records" :key="record.id" data-cy="entityTable">
            <td v-if="hasAnyAuthority('ROLE_ADMIN')">{{ record.active }}</td>
            <td v-if="hasAnyAuthority('ROLE_ADMIN')">
              {{ record.user ? record.user.login : '' }}
            </td>
            <td v-text="$t('calculatorApp.Operator.' + record.operation)">{{ record.operation }}</td>
            <td>{{ record.amount }}</td>
            <td> = {{ roundTo(record.operationResponse,2) }}</td>
            <td> ${{ roundTo(record.userBalance,2) }}</td>
            <td>{{ record.date ? $d(Date.parse(record.date),  'short') : '' }} </td>

            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'RecordView', params: { recordId: record.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(record)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="calculatorApp.record.delete.question" data-cy="recordDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-record-heading" v-text="$t('calculatorApp.record.delete.question', { id: removeId })">
          Are you sure you want to delete this Record?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-record"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeRecord()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="records && records.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./record.component.ts"></script>
