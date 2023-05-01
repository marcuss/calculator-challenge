<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="record">
        <h2 class="jh-entity-heading" data-cy="recordDetailsHeading">
          <span v-text="$t('calculatorApp.record.detail.title')">Record</span> {{ record.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('calculatorApp.record.active')">Active</span>
          </dt>
          <dd>
            <span>{{ record.active }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.record.operation')">Operation</span>
          </dt>
          <dd>
            <span v-text="$t('calculatorApp.Operator.' + record.operation)">{{ record.operation }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.record.amount')">Amount</span>
          </dt>
          <dd>
            <span>{{ record.amount }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.record.userBalance')">User Balance</span>
          </dt>
          <dd>
            <span>{{ record.userBalance }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.record.operationResponse')">Operation Response</span>
          </dt>
          <dd>
            <span>{{ record.operationResponse }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.record.date')">Date</span>
          </dt>
          <dd>
            <span v-if="record.date">{{ $d(Date.parse(record.date), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.record.user')">User</span>
          </dt>
          <dd>
            {{ record.userLogin }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./record-details.component.ts"></script>
