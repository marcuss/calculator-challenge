<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="operation">
        <h2 class="jh-entity-heading" data-cy="operationDetailsHeading">
          <span v-text="$t('calculatorApp.operation.detail.title')">Operation</span> {{ operation.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('calculatorApp.operation.operator')">Operator</span>
          </dt>
          <dd>
            <span v-text="$t('calculatorApp.Operator.' + operation.operator)">{{ operation.operator }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.operation.cost')">Cost</span>
          </dt>
          <dd>
            <span>{{ operation.cost }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="operation.id"
          :to="{ name: 'OperationEdit', params: { operationId: operation.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./operation-details.component.ts"></script>
