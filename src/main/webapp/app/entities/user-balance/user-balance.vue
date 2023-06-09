<template>
  <div>
    <h2 id="page-heading" data-cy="UserBalanceHeading">
      <span v-text="$t('calculatorApp.userBalance.home.title')" id="user-balance-heading">User Balances</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('calculatorApp.userBalance.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'UserBalanceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-user-balance"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('calculatorApp.userBalance.home.createLabel')"> Create a new User Balance </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && userBalances && userBalances.length === 0">
      <span v-text="$t('calculatorApp.userBalance.home.notFound')">No userBalances found</span>
    </div>
    <div class="table-responsive" v-if="userBalances && userBalances.length > 0">
      <table class="table table-striped" aria-describedby="userBalances">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('userLogin')">
              <span v-text="$t('calculatorApp.userBalance.userLogin')">User Login</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'userLogin'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('balance')">
              <span v-text="$t('calculatorApp.userBalance.balance')">Balance</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'balance'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="userBalance in userBalances" :key="userBalance.id" data-cy="entityTable">
            <td>{{ userBalance.userLogin }}</td>
            <td>${{ userBalance.balance }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'UserBalanceView', params: { userBalanceId: userBalance.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'UserBalanceEdit', params: { userBalanceId: userBalance.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(userBalance)"
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
        ><span id="calculatorApp.userBalance.delete.question" data-cy="userBalanceDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-userBalance-heading" v-text="$t('calculatorApp.userBalance.delete.question', { id: removeId })">
          Are you sure you want to delete this User Balance?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-userBalance"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeUserBalance()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="userBalances && userBalances.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-balance.component.ts"></script>
