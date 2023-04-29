<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="calculatorApp.operation.home.createOrEditLabel"
          data-cy="OperationCreateUpdateHeading"
          v-text="$t('calculatorApp.operation.home.createOrEditLabel')"
        >
          Create or edit a Operation
        </h2>
        <div>
          <div class="form-group" v-if="operation.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="operation.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('calculatorApp.operation.operator')" for="operation-operator">Operator</label>
            <select
              class="form-control"
              name="operator"
              :class="{ valid: !$v.operation.operator.$invalid, invalid: $v.operation.operator.$invalid }"
              v-model="$v.operation.operator.$model"
              id="operation-operator"
              data-cy="operator"
              required
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
            <div v-if="$v.operation.operator.$anyDirty && $v.operation.operator.$invalid">
              <small class="form-text text-danger" v-if="!$v.operation.operator.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('calculatorApp.operation.cost')" for="operation-cost">Cost</label>
            <input
              type="number"
              class="form-control"
              name="cost"
              id="operation-cost"
              data-cy="cost"
              :class="{ valid: !$v.operation.cost.$invalid, invalid: $v.operation.cost.$invalid }"
              v-model.number="$v.operation.cost.$model"
              required
            />
            <div v-if="$v.operation.cost.$anyDirty && $v.operation.cost.$invalid">
              <small class="form-text text-danger" v-if="!$v.operation.cost.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.operation.cost.min" v-text="$t('entity.validation.min', { min: 0 })">
                This field should be at least 0.
              </small>
              <small class="form-text text-danger" v-if="!$v.operation.cost.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
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
            :disabled="$v.operation.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./operation-update.component.ts"></script>
