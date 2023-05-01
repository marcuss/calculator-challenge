<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="calculatorApp.userBalance.home.createOrEditLabel"
          data-cy="UserBalanceCreateUpdateHeading"
          v-text="$t('calculatorApp.userBalance.home.createOrEditLabel')"
        >
          Create or edit a UserBalance
        </h2>
        <div>
          <div class="form-group" v-if="userBalance.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="userBalance.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('calculatorApp.userBalance.balance')" for="user-balance-balance">Balance</label>
            <input
              type="number"
              class="form-control"
              name="balance"
              id="user-balance-balance"
              data-cy="balance"
              :class="{ valid: !$v.userBalance.balance.$invalid, invalid: $v.userBalance.balance.$invalid }"
              v-model.number="$v.userBalance.balance.$model"
              required
            />
            <div v-if="$v.userBalance.balance.$anyDirty && $v.userBalance.balance.$invalid">
              <small class="form-text text-danger" v-if="!$v.userBalance.balance.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.userBalance.balance.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('calculatorApp.userBalance.user')" for="user-balance-user">User</label>
            <select class="form-control" id="user-balance-user" data-cy="user" name="user" v-model="userBalance.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="userBalance.user && userOption.id === userBalance.user.id ? userBalance.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.login }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.userBalance.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./user-balance-update.component.ts"></script>
