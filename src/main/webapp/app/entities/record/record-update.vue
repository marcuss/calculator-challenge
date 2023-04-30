<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="calculatorApp.record.home.createOrEditLabel"
          data-cy="RecordCreateUpdateHeading"
          v-text="$t('calculatorApp.record.home.createOrEditLabel')"
        >
          Create or edit a Record
        </h2>
        <div>
          <div class="form-group" v-if="record.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="record.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('calculatorApp.record.operationId')" for="record-operationId">Operation</label>
            <select
              class="form-control"
              name="operationId"
              :class="{ valid: !$v.record.operationId.$invalid, invalid: $v.record.operationId.$invalid }"
              v-model="$v.record.operationId.$model"
              id="record-operationId"
              data-cy="operationId"
            >
              <option
                v-for="operator in operatorValues"
                :key="operator"
                v-bind:value="operator"
                v-bind:label="$t('calculatorApp.Operator.' + operator)"
              >
                {{ operator }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('calculatorApp.record.amount')" for="record-amount">Amount</label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="record-amount"
              data-cy="amount"
              :class="{ valid: !$v.record.amount.$invalid, invalid: $v.record.amount.$invalid }"
              v-model.number="$v.record.amount.$model"
              required
            />
            <div v-if="$v.record.amount.$anyDirty && $v.record.amount.$invalid">
              <small class="form-text text-danger" v-if="!$v.record.amount.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.record.amount.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group"  v-if="hasAnyAuthority('ROLE_ADMIN')" >
            <label class="form-control-label" v-text="$t('calculatorApp.record.user')" for="record-user">User </label>
            <select class="form-control" id="record-user" data-cy="user" name="user" v-model="record.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="record.user && userOption.id === record.user.id ? record.user : userOption"
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
            :disabled="$v.record.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./record-update.component.ts"></script>
