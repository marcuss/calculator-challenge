<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="userBalance">
        <h2 class="jh-entity-heading" data-cy="userBalanceDetailsHeading">
          <span v-text="$t('calculatorApp.userBalance.detail.title')">UserBalance</span> {{ userBalance.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('calculatorApp.userBalance.balance')">Balance</span>
          </dt>
          <dd>
            <span>{{ userBalance.balance }}</span>
          </dd>
          <dt>
            <span v-text="$t('calculatorApp.userBalance.userLogin')">User Login</span>
          </dt>
          <dd>
            <span>{{ userBalance.userLogin }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="userBalance.id"
          :to="{ name: 'UserBalanceEdit', params: { userBalanceId: userBalance.id } }"
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

<script lang="ts" src="./user-balance-details.component.ts"></script>
